package se.fearless.spacedweb.web.constraints;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;

public class ComplexityConstraintValidatorTest {
	@Test
	public void testIsValid() throws Exception {
		ComplexityConstraintValidator validator = new ComplexityConstraintValidator();
		//validator.initialize(null);
		//assertFalse(validator.isValid("poop in face", null));
	}

	@Test
	public void poopTest() {
		Pattern pattern = Pattern.compile("\\d");
		String s = "hello123";
		int count = 0;

		Matcher m = pattern.matcher(s);
		while (m.find()) {
			count++;
		}
		assertEquals(3, count);

	}


}
