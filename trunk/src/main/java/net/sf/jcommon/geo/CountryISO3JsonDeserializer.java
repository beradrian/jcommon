package net.sf.jcommon.geo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CountryISO3JsonDeserializer extends JsonDeserializer<Country> {
	 
	@Override
	public Country deserialize(JsonParser parser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		return Country.getCountries().findByISO3(parser.getText());
	}
	 
}
