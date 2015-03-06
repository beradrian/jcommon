package net.sf.jcommon.web;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;

import net.sf.jcommon.util.ReflectUtils;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public abstract class DomainModelController<T, ID extends Serializable> {

	private static final String FILTER_PARAM = "filter";
	private static final String X_FILTER = "x-{" + FILTER_PARAM + "}";
	private static final String ID_PARAM = "id";

	private static final String HTML_SUFFIX = "";
	private static final String XML_SUFFIX = ".xml";
	private static final String JSON_SUFFIX = ".json";

	/** The name under each the item is stored in the model. */
	private static final String ITEM = "item";
	/** The plural suffix for the item, added to the name under which items are stored in the model. */
	private static final String[] PLURAL_SUFFIX = { "s", "es" };

	protected static final String ALL = "all";
	/** List of items mapping. */
	protected static final String MULTI_VIEW = "list";
	/** Single item edit mapping. */
	protected static final String EDIT = "edit";
	/** Single item delete mapping. */
	protected static final String DELETE = "delete";
	/** Single item view mapping. */
	protected static final String VIEW = "view";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired 
	private ConversionService conversionService;
	
	/** The model class */
	private Class<T> modelClass;

	private Repositories repositories;
	private CrudRepository<T, ID> repository;
	
	@SuppressWarnings("unchecked")
	protected DomainModelController() {
		modelClass = (Class<T>) ReflectUtils.getActualType(getClass(), DomainModelController.class, "T");
		if (modelClass == null) {
			throw new NullPointerException("Cannot determine actual domain model class for controller '" + getClass().getName() + "'");
		}
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(getRepositories().getEntityInformationFor(getModelClass()).getIdType(), 
        		new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                return conversionService.convert(getRepositories().getEntityInformationFor(getModelClass())
                		.getId(getValue()), String.class);
            }

            @Override
            public void setAsText(final String text) {
            	@SuppressWarnings("unchecked")
				T t = getRepository().findOne((ID)conversionService
            			.convert(text, getRepositories().getEntityInformationFor(getModelClass()).getIdType()));
                setValue(t);
            }
        });
    }

	protected Class<T> getModelClass() {
		return modelClass;
	}

	/**
	 * @return the prefix for all the views. By default, it is considered the model class name converted to lower case.
	 */
	protected String getModelName() {
		return getModelClass().getSimpleName().toLowerCase();
	}

	protected Repositories getRepositories() {
		if (repositories != null) {
			return repositories;
		}

		try {
			repositories = applicationContext.getBean(Repositories.class);
		} catch (NoUniqueBeanDefinitionException exc) {
			// if such multiple beans exist then get the first bean
			repositories = applicationContext.getBeansOfType(Repositories.class).entrySet().iterator().next()
					.getValue();
		} catch (NoSuchBeanDefinitionException exc) {
			// create the repositories
			repositories = new Repositories(applicationContext);
		}
		return repositories;
	}

	/**
	 * @return the CRUD repository used by the controller for persistence operations.
	 */
	protected CrudRepository<T, ID> getRepository() {
		if (repository == null) {
			repository = getRepositories().getRepositoryFor(getModelClass());
		}
		return repository;
	}

	/**
	 * View list of items. Populates the model with the items retrieved from persistence layer and forwards to view.
	 * 
	 * @return the view name
	 */
	@RequestMapping(value = {"/", X_FILTER + HTML_SUFFIX})
	public String viewSome(@PathVariable(FILTER_PARAM) String filterName, Model model) {
		Iterable<T> items = filter(filterName);
		model.addAttribute(ITEM + PLURAL_SUFFIX[0], items);
		for (int i = 0; i < PLURAL_SUFFIX.length; i++) {
			model.addAttribute(getModelName() + PLURAL_SUFFIX[i], items);
		}
		return getModelName() + "/" + MULTI_VIEW;
	}

	@RequestMapping(value = { "/" + X_FILTER + JSON_SUFFIX, "/" + X_FILTER + XML_SUFFIX}, method = RequestMethod.GET, 
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody Iterable<T> viewSome(@PathVariable(FILTER_PARAM) String filterName) {
		return filter(filterName);
	}
	
	protected Iterable<T> filter(String filterName) {
		return Iterables.filter(getRepository().findAll(), checkViewAccessPredicate);
	}

	/**
	 * View a single item by its id. Populates the model with the item retrieved from persistence layer and forwards to
	 * view.
	 * 
	 * @return the view name
	 */
	@RequestMapping(value = "/{" + ID_PARAM + "}" + HTML_SUFFIX, method = RequestMethod.GET)
	public String view(@PathVariable(ID_PARAM) ID id, Model model)
			throws InstantiationException, IllegalAccessException {
		T t = null;
		if (id != null) {
			t = getRepository().findOne(id);
		}
		if (t == null) {
			throw new NoSuchItemException(getModelClass(), id);
		}
		
		return view(t, model);
	}
	
	public String view(T t, Model model) {
		checkAccess(t, OperationType.VIEW);
		registerMainItem(t, model);
		return getModelName() + "/" + VIEW;
	}
	
	@RequestMapping(value = {"/{" + ID_PARAM + "}" + XML_SUFFIX, "/{" + ID_PARAM + "}" + JSON_SUFFIX}, 
			method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody T view(@PathVariable(ID_PARAM) ID id) {
		T t = getRepository().findOne(id);
		checkAccess(t, OperationType.VIEW);
		return t;
	}

	/**
	 * The add/edit page single item by its id. Populates the model with the item retrieved from persistence layer and
	 * forwards to view.
	 * 
	 * @param id the item id to edit or null if you want to add an item. This value is injected from request parameters.
	 * @return the view name
	 */
	@RequestMapping(value = "/{" + ID_PARAM + "}" + HTML_SUFFIX, method = RequestMethod.GET, params = EDIT)
	public String addOrEdit(@PathVariable(ID_PARAM) ID id, Model model)
			throws InstantiationException, IllegalAccessException {
		T t;
		if (id != null) {
			t = getRepository().findOne(id);
			if (t == null) {
				throw new NoSuchItemException(getModelClass(), id);
			}
			checkAccess(t, OperationType.UPDATE);
		} else {
			t = getModelClass().newInstance();
			checkAccess(t, OperationType.CREATE);
		}
		registerMainItem(t, model);
		return getModelName() + "/" + EDIT;
	}
	
	@RequestMapping(value = "/" + HTML_SUFFIX, method = RequestMethod.GET, params = EDIT)
	public String add(Model model) throws InstantiationException, IllegalAccessException {
		return addOrEdit(null, model);
	}
	
	protected void registerMainItem(T t, Model model) {
		model.addAttribute(ITEM, t);
		model.addAttribute(getModelName(), t);		
	}

	
	
	/**
	 * Deletes one or more items by their ids. Forwards to list of items upon completion.
	 * 
	 * @return the view name
	 */
	@Transactional
	@RequestMapping(value = "/x-" + DELETE + HTML_SUFFIX, method = RequestMethod.GET)
	public String deleteAndView(@RequestParam(value = ID_PARAM, required = true) ID[] ids) {
		delete(ids);
		return "redirect:.";
	}
	
	/**
	 * Deletes one or more items by their ids. This method is intended to be used from AJAX. 
	 * @return "OK" in case of success
	 */
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public @ResponseBody String delete(@RequestParam(value = ID_PARAM, required = true) ID[] ids) {
		for (ID id : ids) {
			deleteOne(id);
		}
		return "OK";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, params = DELETE)
	public @ResponseBody String deleteByHttpGet(@RequestParam(value = ID_PARAM, required = true) ID[] ids) {
		return delete(ids);
	}
		
	@RequestMapping(value = "/{" + ID_PARAM + "}", method = RequestMethod.GET, params = DELETE)
	public String deleteOneByHttpGetAndView(@PathVariable(ID_PARAM) ID id) {
		deleteOne(id);
		return "redirect:.";
	}
	
	@RequestMapping(value = {"/{" + ID_PARAM + "}" + XML_SUFFIX, "/{" + ID_PARAM + "}" + JSON_SUFFIX}, 
			method = RequestMethod.GET, params = DELETE)
	public @ResponseBody String deleteOneByHttpGet(@PathVariable(ID_PARAM) ID id) {
		deleteOne(id);
		return "OK";
	}
	
	@RequestMapping(value = "/{" + ID_PARAM + "}", method = RequestMethod.DELETE)
	public @ResponseBody String deleteOne(@PathVariable(ID_PARAM) ID id) {
		checkAccess(id, OperationType.DELETE);
		getRepository().delete(id);
		return "OK";
	}

	public enum OperationType { CREATE, UPDATE, DELETE, VIEW}
	
	protected void checkAccess(ID id, OperationType operationType) {
		T t = getRepository().findOne(id);
		if (t == null) {
			throw new NoSuchItemException(getModelClass(), id);
		}
		checkAccess(t, operationType);
	}

	protected void checkAccess(T t, OperationType operationType) {
	}
	
	private Predicate<T> checkViewAccessPredicate = new CheckAccessPredicate(OperationType.VIEW);
			
	private class CheckAccessPredicate implements Predicate<T>  {

		private OperationType operationType;
		
		public CheckAccessPredicate(OperationType operationType) {
			this.operationType = operationType;
		}

		@Override
		public boolean apply(T input) {
			try {
				checkAccess(input, operationType);
			} catch(Throwable t) {
				return false;
			}
			return true;
		}
		
	};
}
