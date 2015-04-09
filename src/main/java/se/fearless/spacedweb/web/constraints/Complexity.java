package se.fearless.spacedweb.web.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ComplexityConstraintValidator.class)
public @interface Complexity {
	String message() default "Not complex enough";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int nrOfDigits() default 1;
}
