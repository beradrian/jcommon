package net.sf.jcommon.spring;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import net.sf.jcommon.web.WebAttribute;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

/** 
 * A Spring WebArgumentResolver which resolves a controller method parameter with an attribute value from
 * request, session or servlet context annotated with {@link net.sf.jcommon.web.WebAttribute}}. 
 * 
 * This is how you can configure it inside a front controller configuration file (x-servlet.xml)
 * 	&lt;bean class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter"&gt;
 *        &lt;property name="customArgumentResolvers"&gt;
 *        	&lt;list&gt;
 *        		&lt;bean class="net.sf.jcommon.spring.WebAttributeArgumentResolver" /&gt;
 *        	&lt;/list&gt;
 *        &lt;/property&gt;
 *	&lt;/bean&gt;
 */
public class WebAttributeArgumentResolver implements WebArgumentResolver, ServletContextAware {

	private ServletContext servletContext;

	@Override
	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		if (!(webRequest instanceof ServletWebRequest))
			return WebArgumentResolver.UNRESOLVED;
		
		ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest;
		WebAttribute annotation = methodParameter.getParameterAnnotation(WebAttribute.class);
		if (annotation != null) {
			Object value = null;
			switch (annotation.scope()) {
				case SESSION:
					HttpSession session = servletWebRequest.getRequest().getSession(false);
					if (session != null) {
						value = session.getAttribute(annotation.value());
					}
					break;
				case APPLICATION:
					value = servletContext.getAttribute(annotation.value());
					break;
				case REQUEST:
				default:
					value = servletWebRequest.getRequest().getAttribute(annotation.value());
			}
			if (value == null) {
				if (annotation.required()) {
					throw new IllegalArgumentException("The attribute " + annotation.value() + " must be specified in " + annotation.scope());
				}
				return null;
			}
			if (!methodParameter.getParameterType().isInstance(value)) {
				throw new ClassCastException("The attribute " + annotation.value() + " is not of the specified type.");
			}
			return value;
		}
		return WebArgumentResolver.UNRESOLVED;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

}
