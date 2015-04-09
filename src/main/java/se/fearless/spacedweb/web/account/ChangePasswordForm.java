package se.fearless.spacedweb.web.account;

import se.fearless.spacedweb.web.constraints.FieldMatch;

@FieldMatch.List({
		@FieldMatch(first = "password", second = "repeatedPassword", message = "The password fields must match")
})
public class ChangePasswordForm {
	private String password;
	private String repeatedPassword;
	private String token;

	public ChangePasswordForm() {

	}

	public ChangePasswordForm(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
