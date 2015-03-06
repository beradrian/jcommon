package net.sf.jcommon.geo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import net.sf.jcommon.geo.Country.BbanLetterCodes;

public class IbanValidator implements ConstraintValidator<Iban, String> {

    private static final long MODULUS = 97;

    private boolean ignoreWhitespace = true;
    
    @Override
	public void initialize(Iban constraintAnnotation) {
    	ignoreWhitespace = constraintAnnotation.ignoreWhitespace();
	}

	@Override
	public boolean isValid(String iban, ConstraintValidatorContext context) {
		if (iban == null || iban.length() < 11) {
			setMessage(context, "length");
			return false;
		}
		
		String reformattedCode = iban.substring(4) + iban.substring(0, 4);
        long total = 0, p10 = 1;
        for (int i = reformattedCode.length() - 1; i >= 0; i--) {
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
            total = (((charValue * p10) % MODULUS) + total) % MODULUS;
            p10 = (p10 * ((charValue > 9 ? 100 : 10) % MODULUS)) % MODULUS;
        }

        if ((total % MODULUS) != 1) {
			setMessage(context, "code");
			return false;        	
        }
			
        Pattern ibanPattern = getIbanPattern(iban);
		if (ibanPattern == null) {
			setMessage(context, "country");
			return false;
		}
		
		if (!ibanPattern.matcher(iban).matches()) {
			setMessage(context, "pattern");
			return false;			
		}
		
		return true;
	}
	
	private static Map<String, Pattern> iso2IbanPattern = new HashMap<String, Pattern>();
	
	private Pattern getIbanPattern(String iban) {
		String iso2 = iban.substring(0, 2);
		Pattern ibanPattern = iso2IbanPattern.get(iso2);
		
		if (ibanPattern == null) {			
			Country c = Country.findByIso(iso2);
			if (c == null) {
				return null;
			}
			ibanPattern = compile(c);
			iso2IbanPattern.put(iso2, ibanPattern);
		}
		return null;
	}
	
    public static Pattern compile(Country c) {
    	return compile(c.getIBAN(), c.getBBAN(), c.getIBANCheckDigits());
    }
    
	/**
     * 
     * @return a regular expression to validate the IBAN.
     */
    public static Pattern compile(String iban, String bban, int ibanCheckDigits) {
		if (iban == null || iban.length() < 2 || bban == null) {
			return null;
		}
		
		return Pattern.compile(iban.substring(0, 2) 
				+ (ibanCheckDigits <= 0 ? "..\\s?" : (ibanCheckDigits < 10 ? "0" : "") + ibanCheckDigits) 
				+ toRegex(bban));
    }
    
    private static String toRegex(String bban) {
    	if (bban == null) {
    		return null;
    	}
		int i = 0;
		StringBuilder sb = new StringBuilder();
		for (String group : bban.split("\\s+")) {
			String g = BbanLetterCodes.valueOf(group.substring(group.length() - 1)).getRegex();
			for (int x = Integer.parseInt(group.substring(0, group.length() - 1)); x > 0; x--) {
				sb.append(g);
				i++;
				if (i % 4 == 0) {
					sb.append("\\s?");
				}
			}
		}
		if (i % 4 == 0) {
			sb.delete(sb.length() - 3, sb.length());
		}
		return sb.toString();
    }
	
	private void setMessage(ConstraintValidatorContext context, String message) {
		if (context == null) {
			return;
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("iban.invalid." + message);
	}

}
