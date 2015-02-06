package net.sf.jcommon.web;

import javax.servlet.http.HttpSession;

import com.google.common.base.Predicate;

public class HttpSessionPredicates {

	/** Use only static methods - this is a factory. */
	private HttpSessionPredicates() {}
	
	public static Predicate<HttpSession> sessionWithAttribute(final String name) {
		return sessionWithAttribute(name, null);
	}
	
	public static Predicate<HttpSession> sessionWithAttribute(final String name, final Object value) {
		return new Predicate<HttpSession>() {
			@Override
			public boolean apply(HttpSession session) {
				Object val = session.getAttribute(name);
				if (val == null) {
					return false;
				}
				return (value == null) || val.equals(value);
			}
			
		};
	}
}
