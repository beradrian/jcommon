package net.sf.jcommon.beans;

import java.util.*;

import net.sf.jcommon.util.LookupContainer;

public class BeanTemplate {

	private String id;
	private String type;
	private List<Object> initArguments = new ArrayList<Object>();
	private Map<String, Object> properties = new HashMap<String, Object>();
	
	public BeanTemplate(String id, String type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}

	public List<Object> getInitArguments() {
		return initArguments;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public Object createInstance() {
		return createInstance(null);
	}
		
	public Object createInstance(LookupContainer<BeanTemplate> container) {
		BeanTemplate parent = null;
		if (container != null)
			 parent = container.find(type);
		Object x = (parent != null ? parent.createInstance(container) 
				: BeanUtils.createBean(type, initArguments));
		if (x != null) {
			BeanUtils.populateProperties(x, properties);
		}
		return x;
	}
}
