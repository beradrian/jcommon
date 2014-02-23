package net.sf.jcommon.web;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WebAttribute {

	public enum SCOPE {REQUEST, SESSION, APPLICATION};
	
	SCOPE scope() default SCOPE.REQUEST;
	
	String value();
	
	boolean required() default false;
}
