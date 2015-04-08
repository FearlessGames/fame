package se.fearless.spacedweb.web.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.fearless.spacedweb.services.NoSuchEmailException;
import se.fearless.spacedweb.services.PasswordResetService;

import javax.validation.Valid;

@Controller
public class RequestNewPasswordMailController {
    private static final String URL = "/resetPassword.html";
    private static final String FORM_VIEW = "resetPassword";
    private final PasswordResetService passwordResetService;
    private static final String SUCCESS_VIEW = "passwordResetMailSent";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public RequestNewPasswordMailController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @ModelAttribute
    public MailForm getMailForm() {
        return new MailForm();
    }

    @RequestMapping(value = URL, method = RequestMethod.GET)
    public String getForm() {
        return FORM_VIEW;
    }

    @RequestMapping(value = URL, method = RequestMethod.POST)
    public String onPost(@Valid MailForm mailForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return FORM_VIEW;
        } else {
            try {
                String email = mailForm.getEmail();
                passwordResetService.requestAndSendResetToken(email);
            } catch (NoSuchEmailException e) {
                bindingResult.addError(new FieldError("emailForm", "email", "No such user"));
                bindingResult.reject("email", "No such user");
                return FORM_VIEW;
            } catch (MailException mailException) {
                log.error("Could not send mail", mailException);
                bindingResult.addError(new FieldError("emailForm", "email", "Mail service temporarily down"));
                bindingResult.reject("email", "Mail service temporarily down");
                return FORM_VIEW;
            }
            return SUCCESS_VIEW;
        }
    }

}
