package net.sf.jcommon.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Registers the instance of this listener as an application attribute.
 * The default attribute name is the class name.
 * It is obvious that it can be only one instance registered under the same name 
 * (the last listener in the deployment descriptor)
 */
public abstract class SelfRegisteredAttributeListener implements ServletContextListener {

	private String attributeName;
	
	public SelfRegisteredAttributeListener() {
		attributeName = this.getClass().getName();
	}

	public SelfRegisteredAttributeListener(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// do nothing - it is not necessary to remove the attribute
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		event.getServletContext().setAttribute(attributeName, this);
	}
	
}
