package se.fearless.spacedweb.web.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.fearless.spacedweb.services.CaptchaService;
import se.fearless.spacedweb.services.EmailOccupiedException;
import se.fearless.spacedweb.services.UserAccountService;
import se.fearless.spacedweb.services.UsernameOccupiedException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class CreateAccountController {
	public static final String FORM_VIEW = "createAccount";
	public static final String SUCCESS_VIEW = "accountCreated";
	private static final String URL = "/createAccount.html";
	private final UserAccountService userAccountService;
	private final CaptchaService captchaService;


	@ModelAttribute()
	public UserAccountForm newRequest() {
		return new UserAccountForm();
	}

	@Autowired
	public CreateAccountController(UserAccountService userAccountService, CaptchaService captchaService) {
		this.userAccountService = userAccountService;
		this.captchaService = captchaService;
	}

	@RequestMapping(value = URL, method = RequestMethod.GET)
	public String getForm() {
		return FORM_VIEW;
	}

	@RequestMapping(value = URL, method = RequestMethod.POST)
	public String onPost(@Valid UserAccountForm userAccountForm, BindingResult bindingResult, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			return FORM_VIEW;
			//} else if (!captchaService.validateCaptcha(request)) {
			//	return FORM_VIEW;
		} else {
			try {
				userAccountService.createAccount(userAccountForm.getUsername(), userAccountForm.getPassword(), userAccountForm.getEmail());
			} catch (UsernameOccupiedException e) {
				bindingResult.addError(new FieldError("userAccountForm", "username", "username already taken"));
				bindingResult.reject("username", "username taken");
				return FORM_VIEW;
			} catch (EmailOccupiedException e) {
				bindingResult.addError(new FieldError("userAccountForm", "email", "email already taken"));
				bindingResult.reject("email", "email taken");
				return FORM_VIEW;
			}
			return SUCCESS_VIEW;
		}
	}


}
