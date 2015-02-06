package net.sf.jcommon.geo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CountryISO3JsonSerializer extends JsonSerializer<Country> {
	 
	@Override
	public void serialize(Country value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
  		jgen.writeString(value.getISO3());
	 }
	 
}
