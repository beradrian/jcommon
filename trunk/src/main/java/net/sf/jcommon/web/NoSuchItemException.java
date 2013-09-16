package net.sf.jcommon.web;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoSuchItemException extends RuntimeException {

	private Class<?> itemClass;
	private Object itemId;
	
	public NoSuchItemException(Class<?> itemClass, Object itemId) {
		this.itemClass = itemClass;
		this.itemId = itemId;
	}

	public Class<?> getItemClass() {
		return itemClass;
	}

	public Object getItemId() {
		return itemId;
	}
	
}
