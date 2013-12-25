package net.sf.jcommon.geo;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Predicate;

public class IbanValidator implements ConstraintValidator<Iban, String>, Predicate<String> {

    private static final long MAX = 999999999, MODULUS = 97;

    private boolean ignoreWhitespace = true;
    private Map<String, String> messages = new HashMap<String, String>();
    
    @Override
	public void initialize(Iban constraintAnnotation) {
    	ignoreWhitespace = constraintAnnotation.ignoreWhitespace();
    	messages.put("code", constraintAnnotation.invalidCodeMessage());
    	messages.put("country", constraintAnnotation.invalidCountryMessage());
    	messages.put("pattern", constraintAnnotation.invalidPatternMessage());
	}

	@Override
	public boolean isValid(String iban, ConstraintValidatorContext context) {
		if (iban == null || iban.length() < 11) {
			setMessage(context, "pattern");
			return false;
		}
		
		String reformattedCode = iban.substring(4) + iban.substring(0, 4);
        long total = 0;
        for (int i = 0; i < reformattedCode.length(); i++) {
        	char ch = reformattedCode.charAt(i);
        	if (Character.isWhitespace(ch)) {
        		if (ignoreWhitespace) {
        			continue;
        		} else {
                	setMessage(context, "pattern");
                    return false;
        		}        		
        	}
            int charValue = (ch >= '0' && ch <= '9' ? ch - '0' : (ch - 'A' + 10));
            if (charValue < 0 || charValue > 35) {
            	setMessage(context, "pattern");
                return false;
            }
            total = (charValue > 9 ? total * 100 : total * 10) + charValue;
            if (total > MAX) {
                total = (total % MODULUS);
            }
        }

        if ((total % MODULUS) != 1) {
			setMessage(context, "code");
			return false;        	
        }
			
		Country c = Country.getCountries().findByISO2(iban.substring(0, 2));
		if (c == null) {
			setMessage(context, "country");
			return false;
		}
		
		if (!c.getIBANPattern().matcher(iban).matches()) {
			setMessage(context, "pattern");
			return false;			
		}
		
		return true;
	}
	
	private void setMessage(ConstraintValidatorContext context, String key) {
		if (context == null) {
			return;
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messages.get(key));
	}

	@Override
	public boolean apply(String iban) {
		return isValid(iban, null);
	}

}
