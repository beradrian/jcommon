package net.sf.jcommon.persistence;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Converter(autoApply = true)
public class GrantedAuthorityToStringAttributeConverter implements AttributeConverter<GrantedAuthority, String> {
	@Override
	public String convertToDatabaseColumn(GrantedAuthority attribute) {
		return attribute.getAuthority();
	}

	@Override
	public GrantedAuthority convertToEntityAttribute(String dbData) {
		return new SimpleGrantedAuthority(dbData);
	}
}
