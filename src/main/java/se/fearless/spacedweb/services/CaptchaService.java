package se.fearless.spacedweb.services;

import javax.servlet.http.HttpServletRequest;

public interface CaptchaService {
    boolean validateCaptcha(HttpServletRequest request);
     String renderToHtml();
}
