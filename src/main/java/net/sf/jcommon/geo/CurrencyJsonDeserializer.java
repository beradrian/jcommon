package net.sf.jcommon.geo;

import java.io.IOException;
import java.util.Currency;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CurrencyJsonDeserializer extends JsonDeserializer<Currency> {
	 
	@Override
	public Currency deserialize(JsonParser parser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		return Currency.getInstance(parser.getText());
	}
	 
}
