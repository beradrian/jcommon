package net.sf.jcommon.geo;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IbanValidator.class)
@Documented
public @interface Iban {
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
    
	String message() default "{net.sf.jcommon.geo.Iban.message}";
	String invalidCodeMessage() default "{net.sf.jcommon.geo.Iban.code.message}";
	String invalidCountryMessage() default "{net.sf.jcommon.geo.Iban.country.message}";
	String invalidPatternMessage() default "{net.sf.jcommon.geo.Iban.pattern.message}";
	boolean ignoreWhitespace() default true;
}