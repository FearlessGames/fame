package se.fearless.spacedweb.web.account;

import se.fearless.spacedweb.web.constraints.FieldMatch;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldMatch.List({
        @FieldMatch(first = "password", second = "repeatedPassword", message = "The password fields must match")
})
public class UserAccountForm {
    @Size(min = 4, max = 20, message = "required field, useraccount must be at least 4 characters but not more then 20")
    private String username;

    @Size(min = 6, max = 30, message = "required field, password must be at least 6 characters but not more then 30")
    private String password;

    private String repeatedPassword;

    @NotNull(message = "Faulty email")
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Faulty email")
    private String email;

    public UserAccountForm(String username, String password, String repeatedPassword, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.repeatedPassword = repeatedPassword;
    }

    public UserAccountForm() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }
}