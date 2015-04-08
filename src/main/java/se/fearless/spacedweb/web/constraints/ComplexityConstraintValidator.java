package se.fearless.spacedweb.web.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexityConstraintValidator implements ConstraintValidator<Complexity, String> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Pattern pattern;
    private int nrOfDigits;

    public ComplexityConstraintValidator() {
        pattern = Pattern.compile("\\d");
    }

    @Override
    public void initialize(Complexity complexity) {
        nrOfDigits = complexity.nrOfDigits();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Matcher m = pattern.matcher(s);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count >= nrOfDigits;
    }
}
