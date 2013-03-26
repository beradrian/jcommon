package net.sf.jcommon.geo;

import java.beans.PropertyEditorSupport;

/** Editor for {@code Country}. Expects the country ISO code (two letters). 
 * In Spring can be registered this way:
 * {@code
 * 		<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
 *			<property name="customEditors">
 *				<map>
 *					<entry key="net.sf.jcommon.util.Country">
 *						<bean class="net.sf.jcommon.util.CountryPropertyEditor"/>
 *					</entry>
 *				</map>
 *			</property>
 *		</bean>
 * }
 * @deprecated Use {@link CountryFormatter} instead
 */
@Deprecated
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
		Country cc = Country.getCountries().findByISO(text);
		if (cc == null)
			throw new IllegalArgumentException("Country code is invalid");
		setValue(cc);
	}
	
}
