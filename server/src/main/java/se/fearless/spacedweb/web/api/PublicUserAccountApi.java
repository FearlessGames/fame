package se.fearless.spacedweb.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.security.FameAuthTokenService;
import se.fearless.spacedweb.services.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/public/users")
public class PublicUserAccountApi {
	private final AuthenticationSaltService authenticationSaltService;
	private final FameAuthTokenService fameAuthTokenService;
	private final UserAccountService userAccountService;
    private final ReCaptchaService reCaptchaService;
    private final PasswordResetService passwordResetService;

    @Autowired
	public PublicUserAccountApi(AuthenticationSaltService authenticationSaltService, FameAuthTokenService fameAuthTokenService, UserAccountService userAccountService, ReCaptchaService reCaptchaService, PasswordResetService passwordResetService) {
		this.authenticationSaltService = authenticationSaltService;
		this.fameAuthTokenService = fameAuthTokenService;
		this.userAccountService = userAccountService;
		this.reCaptchaService = reCaptchaService;
        this.passwordResetService = passwordResetService;
    }

    @RequestMapping(value = "/{userName}/salt", method = RequestMethod.GET)
    public Salts requestSalts(@PathVariable("userName") String userName) {
        return authenticationSaltService.requestSalts(userName);
    }

	@RequestMapping(value = "/{userName}/auth/{feature}/{hash}", method = RequestMethod.GET)
	public ResponseEntity<FameAuthToken> auth(@PathVariable("userName") String userName, @PathVariable("feature") String feature, @PathVariable("hash") String hash) {

		return userAccountService.authenticate(userName, hash)
				.filter(userAccount -> userAccountService.isAccountAuthorizedForFeature(userAccount, feature))
				.map(userAccount -> new ResponseEntity<>(new FameAuthToken(fameAuthTokenService.createFameAuthToken(userAccount, feature)), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));

	}

	@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody CreateUserDTO createUserDTO, HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        boolean valid = reCaptchaService.validateCaptcha(remoteAddress, createUserDTO.recaptchaResponseField);
        if (!valid) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

		try {
			userAccountService.createAccount(createUserDTO.username, createUserDTO.password, createUserDTO.email);
		} catch (EmailOccupiedException | UsernameOccupiedException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{email}/sendResetToken", method = RequestMethod.GET)
	public void sendResetToken(@PathVariable("email") String email) {
        try {
            passwordResetService.requestAndSendResetToken(email);
        } catch (NoSuchEmailException ignored) {

        }
    }

	@RequestMapping(value = "/{userName}/resetPassword", method = RequestMethod.POST)
	public void resetPassword(@PathVariable("userName") String userName, ResetPasswordDTO passwordDTO) {
        try {
            passwordResetService.changePassword(passwordDTO.token, passwordDTO.password);
        } catch (IllegalTokenException e) {
            e.printStackTrace();
        }
    }



    private static class ResetPasswordDTO {
		public String password;
		public String token;
	}

    private static class CreateUserDTO {
		public String username;
		public String email;
		public String password;
		public String recaptchaResponseField;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRecaptchaResponseField() {
            return recaptchaResponseField;
        }

        public void setRecaptchaResponseField(String recaptchaResponseField) {
            this.recaptchaResponseField = recaptchaResponseField;
        }
    }

	private static class FameAuthToken {
		public String token;

		public FameAuthToken(String token) {
			this.token = token;
		}
	}

}
