package se.fearless.spacedweb.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class UserApi {
    private final AuthenticationSaltService authenticationSaltService;
    private final UserAccountService userAccountService;
    private final ReCaptchaService reCaptchaService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public UserApi(AuthenticationSaltService authenticationSaltService, UserAccountService userAccountService, ReCaptchaService reCaptchaService, PasswordResetService passwordResetService) {
        this.authenticationSaltService = authenticationSaltService;
        this.userAccountService = userAccountService;
        this.reCaptchaService = reCaptchaService;
        this.passwordResetService = passwordResetService;
    }

	@RequestMapping(value = "/api/public/users/{userName}/salt", headers = "Accept=application/json", method = RequestMethod.GET)
	public Salts requestSalts(@PathVariable("userName") String userName) {
        return authenticationSaltService.requestSalts(userName);
    }


	@RequestMapping(value = "/api/public/users/{userName}/auth/{hash}", method = RequestMethod.GET)
	public UserAccountDTO auth(@PathVariable("userName") String userName, @PathVariable("hash") String hash) {
        UserAccount userAccount = userAccountService.authenticate(userName, hash);
        return new UserAccountDTO(userAccount.getUsername(), userAccount.getEmail(), userAccount.getTimeOfRegistration(), userAccount.getFeatures());
    }

    @RequestMapping(value = "/api/public/users", method = RequestMethod.POST)
    public ResponseEntity<?> createWithCaptcha(CreateUserDTO createUserDTO, HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        boolean valid = reCaptchaService.validateCaptcha(remoteAddress, createUserDTO.recaptchaChallengeField, createUserDTO.recaptchaChallengeField);
        if (!valid) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return createUser(createUserDTO);

    }

    @RequestMapping(value = "/api/private/users", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody CreateUserDTO createUserDTO) {
        return createUser(createUserDTO);
    }

    private ResponseEntity<?> createUser(CreateUserDTO createUserDTO) {
        try {
            userAccountService.createAccount(createUserDTO.username, createUserDTO.password, createUserDTO.email);
        } catch (EmailOccupiedException | UsernameOccupiedException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/public/users/{email}/sendResetToken", method = RequestMethod.GET)
    public void sendResetToken(@PathVariable("email") String email) {
        try {
            passwordResetService.requestAndSendResetToken(email);
        } catch (NoSuchEmailException ignored) {

        }
    }

    @RequestMapping(value = "/api/public/users/{userName}/resetPassword", method = RequestMethod.POST)
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
		public String recaptchaChallengeField;
		public String recaptchaResponseField;
	}

    private static class UserAccountDTO {
		public String username;
		public String email;
		public Date timeOfRegistration;
		public List<String> features;

        public UserAccountDTO(String username, String email, Date timeOfRegistration, List<String> features) {
            this.username = username;
            this.email = email;
            this.timeOfRegistration = timeOfRegistration;
            this.features = features;
        }
    }
}
