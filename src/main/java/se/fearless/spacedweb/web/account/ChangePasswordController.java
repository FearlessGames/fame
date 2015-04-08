package se.fearless.spacedweb.web.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.fearless.spacedweb.services.IllegalTokenException;
import se.fearless.spacedweb.services.PasswordResetService;

import javax.validation.Valid;

@Controller
public class ChangePasswordController {
    private static final String URL = "/changePassword.html";
    private static final String FORM_VIEW = "changePassword";
    private final PasswordResetService passwordResetService;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private static final String SUCCESS_VIEW = "passwordChanged";
    private static final String INVALID_TOKEN_VIEW = "invalidToken";

    @Autowired
    public ChangePasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @RequestMapping(value = URL, method = RequestMethod.GET)
    public String getChangePasswordForm(ModelMap model, String token) {
        log.debug("got password change request with token: {}", token);

        if (!passwordResetService.isValidToken(token)) {
            return INVALID_TOKEN_VIEW;
        }

        if (model.get("changePasswordForm") == null) {
            model.addAttribute("changePasswordForm", new ChangePasswordForm(token));
        }
        return FORM_VIEW;
    }

    @RequestMapping(value = URL, method = RequestMethod.POST)
    public String onPost(@Valid ChangePasswordForm changePasswordForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_VIEW;
        } else {
            try {
                passwordResetService.changePassword(changePasswordForm.getToken(), changePasswordForm.getPassword());
            } catch (IllegalTokenException e) {
                log.warn("someone used an unexisting token to try and change password"); //todo: throttle?
                bindingResult.addError(new FieldError("changePasswordForm", "password", "Service temporarily down"));
                bindingResult.reject("password", "Service temporarily down");
                return FORM_VIEW;
            }
            return SUCCESS_VIEW;
        }
    }

}
