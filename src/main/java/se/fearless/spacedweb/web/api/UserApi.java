package se.fearless.spacedweb.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.services.AuthenticationSaltService;
import se.fearless.spacedweb.services.UserAccountService;

import java.util.Date;
import java.util.List;

@RestController
public class UserApi {
    private final AuthenticationSaltService authenticationSaltService;
    private final UserAccountService userAccountService;

    @Autowired
    public UserApi(AuthenticationSaltService authenticationSaltService, UserAccountService userAccountService) {
        this.authenticationSaltService = authenticationSaltService;
        this.userAccountService = userAccountService;
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
