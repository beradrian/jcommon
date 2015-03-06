package net.sf.jcommon.geo.persistence;

import java.io.IOException;

import net.sf.jcommon.geo.Country;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CountryIsoJsonDeserializer extends JsonDeserializer<Country> {
	 
	@Override
	public Country deserialize(JsonParser parser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		return Country.findByIso(parser.getText());
	}
	 
}
