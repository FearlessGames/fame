package se.fearless.spacedweb.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @Autowired
    public UserApi(AuthenticationSaltService authenticationSaltService, UserAccountService userAccountService, ReCaptchaService reCaptchaService) {
        this.authenticationSaltService = authenticationSaltService;
        this.userAccountService = userAccountService;
        this.reCaptchaService = reCaptchaService;
    }

    @RequestMapping(value = "/api/rest/users/{userName}/salt", method = RequestMethod.GET)
    public Salts requestSalts(@PathVariable("userName") String userName) {
        return authenticationSaltService.requestSalts(userName);
    }


    @RequestMapping(value = "/api/rest/users/{userName}/auth/{hash}", method = RequestMethod.GET)
    public UserAccountDTO auth(@PathVariable("userName") String userName, @PathVariable("hash") String hash) {
        UserAccount userAccount = userAccountService.authenticate(userName, hash);
        return new UserAccountDTO(userAccount.getUsername(), userAccount.getEmail(), userAccount.getTimeOfRegistration(), userAccount.getFeatures());
    }

    @RequestMapping(value = "/api/res/users", method = RequestMethod.POST)
    public CreatedUserResponseDTO create(CreateUserDTO createUserDTO, HttpServletRequest request) {
        String remoteAddress = request.getRemoteAddr();
        boolean valid = reCaptchaService.validateCaptcha(remoteAddress, createUserDTO.recaptchaChallengeField, createUserDTO.recaptchaChallengeField);
        if (!valid) {
            return new CreatedUserResponseDTO();
        }

        try {
            userAccountService.createAccount(createUserDTO.username, createUserDTO.password, createUserDTO.email);
        } catch (EmailOccupiedException e) {
            return new CreatedUserResponseDTO();
        } catch (UsernameOccupiedException e) {
            return new CreatedUserResponseDTO();
        }

        return new CreatedUserResponseDTO();


    }


    private static class CreateUserDTO {
        private String username;
        private String email;
        private String password;
        private String recaptchaChallengeField;
        private String recaptchaResponseField;
    }

    private static class CreatedUserResponseDTO {
        private boolean createdUser;
        private String message;
    }


    private static class UserAccountDTO {
        private String username;
        private String email;
        private Date timeOfRegistration;
        private List<String> features;

        public UserAccountDTO(String username, String email, Date timeOfRegistration, List<String> features) {
            this.username = username;
            this.email = email;
            this.timeOfRegistration = timeOfRegistration;
            this.features = features;
        }
    }
}
