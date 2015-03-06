package net.sf.jcommon.geo.persistence;

import java.io.IOException;

import net.sf.jcommon.geo.Country;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CountryIsoJsonSerializer extends JsonSerializer<Country> {
	 
	@Override
	public void serialize(Country value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
  		jgen.writeString(value.getIso());
	 }

}
