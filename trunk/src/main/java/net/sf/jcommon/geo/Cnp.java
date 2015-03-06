package net.sf.jcommon.geo;

import java.lang.annotation.*;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CnpValidator.class)
@Documented
public @interface Cnp {
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
    
	String message() default "cnp.invalid";
}