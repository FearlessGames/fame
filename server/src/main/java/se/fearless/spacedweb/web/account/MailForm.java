package se.fearless.spacedweb.web.account;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class MailForm {
	@NotNull(message = "required field")
	@Pattern(regexp = ".+@.+\\.[a-z]+")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
