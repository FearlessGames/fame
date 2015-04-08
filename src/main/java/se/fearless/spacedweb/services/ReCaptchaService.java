package se.fearless.spacedweb.services;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class ReCaptchaService implements CaptchaService {
    private final static String publicKey = "6LfVH8cSAAAAAGWlBywDLqdMwNR7NfH2_5NEXFag";
    private final static String privateKey = "6LfVH8cSAAAAAEC7ZmWcRhxMXeirSQFcdR96x1f2";

    @Override
    public boolean validateCaptcha(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(privateKey);
        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);
        return reCaptchaResponse.isValid();
    }

    public String renderToHtml() {
        ReCaptcha c = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, false);
        return c.createRecaptchaHtml(null, null);
    }
}
