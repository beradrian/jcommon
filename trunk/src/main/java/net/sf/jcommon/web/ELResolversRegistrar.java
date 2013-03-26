package net.sf.jcommon.web;

import javax.el.ELResolver;
import javax.servlet.ServletContext;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;

import org.springframework.web.context.ServletContextAware;

/** Registers the specified ELResolver's in the JSP EL context. */
public class ELResolversRegistrar implements ServletContextAware {

	/**
	 * The EL resolvers to be registered.
	 */
	private ELResolver[] resolvers;
	
	/** 
	 * Creates a registrar with the given resolvers.
	 * @param resolvers the EL resolvers to be registered 
	 */
	public ELResolversRegistrar(ELResolver... resolvers) {
		this.resolvers = resolvers;
	}

	/** 
	 * Creates a registrar with the given resolver.
	 * @param resolver the EL resolver to be registered 
	 */
	public ELResolversRegistrar(ELResolver resolver) {
		this(new ELResolver[]{resolver});
	}

	public void setServletContext(ServletContext servletContext) {
        JspApplicationContext jspContext = JspFactory.getDefaultFactory().getJspApplicationContext(servletContext);
        for (ELResolver resolver : resolvers) {
        	jspContext.addELResolver(resolver);
        }
	}

}
