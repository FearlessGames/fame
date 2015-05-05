package se.fearless.spacedweb.web.account;

import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import se.fearless.spacedweb.services.CaptchaService;
import se.fearless.spacedweb.services.EmailOccupiedException;
import se.fearless.spacedweb.services.UserAccountService;
import se.fearless.spacedweb.services.UsernameOccupiedException;
import se.fearless.spacedweb.web.FormValidator;
import se.mockachino.annotations.Mock;

import static junit.framework.Assert.assertEquals;
import static se.mockachino.Mockachino.*;
import static se.mockachino.matchers.Matchers.any;


public class CreateAccountControllerTest {
	@Mock
	private FormValidator<UserAccountForm> formValidator;
	@Mock
	private UserAccountService userAccountService;
	@Mock
	BindingResult bindingResult;

	@Mock
	private CaptchaService captchaService;

	@Before
	public void setUp() {
		setupMocks(this);
		//when(captchaService.validateCaptcha(any(HttpServletRequest.class))).thenReturn(true);
	}

	@Test
	public void testWillCreateAccountOnValidationOk() throws Exception {
		CreateAccountController cac = new CreateAccountController(userAccountService, captchaService);
		UserAccountForm userForm = new UserAccountForm("username", "password", "password", "email");
		when(bindingResult.hasErrors()).thenReturn(Boolean.FALSE);
		String result = cac.onPost(userForm, bindingResult, null);
		verifyOnce().on(userAccountService).createAccount(any(String.class), any(String.class), any(String.class));
		assertEquals(cac.SUCCESS_VIEW, result);
	}

	@Test
	public void testWillNotCreateAccountOnValidationErrors() throws UsernameOccupiedException, EmailOccupiedException {
		CreateAccountController cac = new CreateAccountController(userAccountService, captchaService);
		UserAccountForm userForm = new UserAccountForm("username", "password", "password", "email");
		when(bindingResult.hasErrors()).thenReturn(Boolean.TRUE);
		String result = cac.onPost(userForm, bindingResult, null);
		verifyNever().on(userAccountService).createAccount(any(String.class), any(String.class), any(String.class));
		assertEquals(cac.FORM_VIEW, result);
	}
}
