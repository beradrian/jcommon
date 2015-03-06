package net.sf.jcommon.geo.persistence;

import java.io.IOException;
import java.util.Currency;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CurrencyJsonSerializer extends JsonSerializer<Currency> {
	 
	@Override
	public void serialize(Currency value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
  		jgen.writeString(value.getCurrencyCode());
	 }
	 
}
