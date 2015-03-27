package net.sf.jcommon.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ModelAttributeInterceptor extends HandlerInterceptorAdapter {
	
	private String name;
	private Object value;
	
	public ModelAttributeInterceptor(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			modelAndView.addObject(name, value);
		}
	}

}
