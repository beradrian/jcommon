package net.sf.jcommon.web;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class DomainModelController<T, ID extends Serializable> {

	private static final String ID_PARAM = "id";
	/** The name under each the item is stored in the model. */
	private static final String ITEM = "item";
	/** The plural suffix for the item, added to the name under which items are stored in the model. */
	private static final String[] PLURAL_SUFFIX = { "s", "es" };

	/** List of items mapping. */
	protected static final String LIST = "list";
	/** Single item edit mapping. */
	protected static final String EDIT = "edit";
	/** Single item delete mapping. */
	protected static final String DELETE = "delete";
	/** Single item view mapping. */
	protected static final String VIEW = "view";

	@Autowired
	private ApplicationContext applicationContext;

	/** The model class */
	private Class<T> modelClass;

	private Repositories repositories;
	private CrudRepository<T, ID> repository;

	@SuppressWarnings("unchecked")
	protected DomainModelController() {
		// this is necessary because of code injection for Spring MVC Controller
		// in case that the super class is a class (most probably injected by Spring, then get the super of that super
		// class
		// this way we will move up the hierarchy, one level before DomainModelController
		Class<?> superClass = this.getClass();
		while (superClass.getSuperclass() != DomainModelController.class) {
			superClass = superClass.getSuperclass();
		}

		// determine the model class by finding the parameterized type of this actual controller class
		Type genericSuperClass = superClass.getGenericSuperclass();
		// the model class is the class of the first generic argument
		modelClass = (Class<T>) ((ParameterizedType) genericSuperClass).getActualTypeArguments()[0];
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
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
	@RequestMapping(value = "/" + LIST)
	public String list(Model model) {
		Iterable<T> items = getRepository().findAll();
		model.addAttribute(ITEM + PLURAL_SUFFIX[0], items);
		for (int i = 0; i < PLURAL_SUFFIX.length; i++) {
			model.addAttribute(getModelName() + PLURAL_SUFFIX[i], items);
		}
		return getModelName() + "/" + LIST;
	}

	/**
	 * View a single item by its id. Populates the model with the item retrieved from persistence layer and forwards to
	 * view.
	 * 
	 * @return the view name
	 */
	@RequestMapping(value = "/" + VIEW, method = RequestMethod.GET)
	public String view(@RequestParam(value = ID_PARAM, required = false) ID id, Model model)
			throws InstantiationException, IllegalAccessException {
		T t = null;
		if (id != null) {
			t = getRepository().findOne(id);
		}
		if (t == null) {
			throw new NoSuchItemException(getModelClass(), id);
		}

		model.addAttribute(ITEM, t);
		model.addAttribute(getModelName(), t);
		return getModelName() + "/" + VIEW;
	}

	/**
	 * The add/edit page single item by its id. Populates the model with the item retrieved from persistence layer and
	 * forwards to view.
	 * 
	 * @param id the item id to edit or null if you want to add an item. This value is injected from request parameters.
	 * @return the view name
	 */
	@RequestMapping(value = "/" + EDIT, method = RequestMethod.GET)
	public String addOrEdit(@RequestParam(value = ID_PARAM, required = false) ID id, Model model)
			throws InstantiationException, IllegalAccessException {
		T t;
		if (id != null) {
			t = getRepository().findOne(id);
			if (t == null) {
				throw new NoSuchItemException(getModelClass(), id);
			}
		} else {
			t = getModelClass().newInstance();
		}

		model.addAttribute(ITEM, t);
		model.addAttribute(getModelName(), t);
		return getModelName() + "/" + EDIT;
	}

	/**
	 * Deletes one or more items by their ids. Forwards to list of items upon completion.
	 * 
	 * @return the view name
	 */
	@Transactional
	@RequestMapping(value = "/" + DELETE, method = RequestMethod.GET)
	public String delete(@RequestParam(value = ID_PARAM, required = true) ID[] ids) {
		for (ID id : ids) {
			getRepository().delete(id);
		}
		return "redirect:" + LIST;
	}

}
