package se.fearless.spacedweb.web.account;

import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.Assert.assertEquals;

public class UserAccountFormValidationTest {

    @Test
    public void testValidation() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        UserAccountForm userAccountForm = new UserAccountForm("username", null, null, "some@gmail.com");

        userAccountForm.setPassword("test");
        userAccountForm.setRepeatedPassword("test");

        assertEquals(validator.validate(userAccountForm).size(), 1);

        userAccountForm.setPassword("testpassword");
        assertEquals(validator.validate(userAccountForm).size(), 1);

        userAccountForm.setRepeatedPassword("testpassword");
        assertEquals(validator.validate(userAccountForm).size(), 0);

    }
}
