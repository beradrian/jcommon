package net.sf.jcommon.geo;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class CountryISO3JsonDeserializer extends JsonDeserializer<Country> {
	 
	@Override
	public Country deserialize(JsonParser parser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		return Country.getCountries().findByISO3(parser.getText());
	}
	 
}
