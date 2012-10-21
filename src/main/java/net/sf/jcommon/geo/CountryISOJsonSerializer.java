package net.sf.jcommon.geo;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CountryISOJsonSerializer extends JsonSerializer<Country> {
	 
	@Override
	public void serialize(Country value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
  		jgen.writeString(value.getISO());
	 }
	 
}
