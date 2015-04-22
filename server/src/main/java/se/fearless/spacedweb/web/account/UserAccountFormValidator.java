package se.fearless.spacedweb.web.account;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import se.fearless.spacedweb.web.FormValidator;

@Service
public class UserAccountFormValidator implements FormValidator<UserAccountForm> {
	private static final String PASSWORD_FIELD = "password";

	public void validate(UserAccountForm formData, Errors errors) {
		if (!formData.getPassword().equals(formData.getRepeatedPassword())) {
			errors.rejectValue(PASSWORD_FIELD, "missmatch", "password missmatch");
		}
	}
}
