package net.sf.jcommon.persistence;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.security.core.GrantedAuthority;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Converter
public class CollectionOfGrantedAuthorityToStringAttributeConverter implements AttributeConverter<Collection<GrantedAuthority>, String> {

	private AttributeConverter<GrantedAuthority, String> itemConverter;
	private String separator;
	
	private Function<GrantedAuthority, String> convertItemToDatabaseColumn = new Function<GrantedAuthority, String>() {
		@Override
		public String apply(GrantedAuthority input) {
			return itemConverter.convertToDatabaseColumn(input);
		}
	};
	
	private Function<String, GrantedAuthority> convertToItem = new Function<String, GrantedAuthority>() {
		@Override
		public GrantedAuthority apply(String input) {
			return itemConverter.convertToEntityAttribute(input);
		}
	};	
		
	public CollectionOfGrantedAuthorityToStringAttributeConverter() {
		this(new GrantedAuthorityToStringAttributeConverter(), ",");
	}
		
	public CollectionOfGrantedAuthorityToStringAttributeConverter(AttributeConverter<GrantedAuthority, String> itemConverter, String separator) {
		this.itemConverter = itemConverter;
		this.separator = separator;
	}

	public String getSeparator() {
		return separator;
	}	
	
	@Override
	public String convertToDatabaseColumn(Collection<GrantedAuthority> attributeObject) {
		return attributeObject == null ? null : Joiner.on(getSeparator())
				.join(Iterables.transform(attributeObject, convertItemToDatabaseColumn));
	}

	@Override
	public Collection<GrantedAuthority> convertToEntityAttribute(String datastoreValue) {
		if (datastoreValue == null)
			return null;
		Collection<GrantedAuthority> values = new HashSet<GrantedAuthority>();
		if (datastoreValue.length() == 0)
			return values;
		Iterables.addAll(values, Iterables.transform(Splitter.on(getSeparator()).split(datastoreValue), convertToItem));
		return values;
	}

}
