package se.fearless.spacedweb.web.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.services.UserAccountService;

import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String expectedServiceKey;
    protected final UserAccountService userAccountService;

    public AuthenticationController(UserAccountService userAccountService, String expectedServiceKey) {
        this.userAccountService = userAccountService;
        this.expectedServiceKey = expectedServiceKey;
    }

    protected void authService(String suppliedServiceKey) {
        if (!expectedServiceKey.equals(suppliedServiceKey)) {
            throw new IllegalArgumentException("Incorrect service key");
        }
    }

    protected void setAuthFailedResponse(HttpServletResponse response) {
        log.warn("setting response unauthorized");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    protected void authorize(HttpServletResponse response, Model model, String feature, UserAccount userAccount) {
        if (userAccount != null && userAccountService.isAccountAuthorizedForFeature(userAccount, feature)) {
            log.debug("account is authorized for feature: " + feature);
            model.addAttribute("userAccount", userAccount);
        } else {
            log.warn("account is NOT authorized for feature: " + feature);
            setAuthFailedResponse(response);
        }
    }
}
