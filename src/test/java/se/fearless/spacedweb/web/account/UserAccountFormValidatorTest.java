package se.fearless.spacedweb.web.account;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;
import se.mockachino.annotations.Mock;

import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;


public class UserAccountFormValidatorTest {
	@Mock
	Errors errors;

	@Before
	public void setUp() {
		setupMocks(this);
	}

	@Test
	public void testValidateNoErrorHappyPath() throws Exception {
		UserAccountFormValidator validator = new UserAccountFormValidator();
		UserAccountForm form = new UserAccountForm("olle", "pw1", "pw1", "email");
		validator.validate(form, errors);
		verifyNever().on(errors).rejectValue(any(String.class), any(String.class), any(String.class));
	}

	@Test
	public void testValidateErrorOnPwdMissmatch() throws Exception {
		UserAccountFormValidator validator = new UserAccountFormValidator();
		UserAccountForm form = new UserAccountForm("olle", "pw1", "pw2", "email");
		validator.validate(form, errors);
		verifyOnce().on(errors).rejectValue(any(String.class), any(String.class), any(String.class));
	}
}
