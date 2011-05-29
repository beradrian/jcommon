package net.sf.jcommon.util;

import java.beans.PropertyEditorSupport;

/** Editor for {@code Country}. Expects the country ISO code (two letters). */
public class CountryPropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		Object value = getValue();
		return (value instanceof Country ? ((Country)value).getISO() : "");
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null) {
			setValue(null);
			return;
		}
		text = text.trim();
		if (text.length() == 0) {
			setValue(null);
			return;
		}
		Country cc = Country.findByISO(text);
		if (cc == null)
			throw new IllegalArgumentException("Country code is invalid");
		setValue(cc);
	}
	
}
