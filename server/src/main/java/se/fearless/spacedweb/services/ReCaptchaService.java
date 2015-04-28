package se.fearless.spacedweb.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ReCaptchaService implements CaptchaService {
    private final static String publicKey = "6LdQCAYTAAAAAN64Mg8pkupmxT0EcA5nQWuF-lES";
    private final static String privateKey = "6LdQCAYTAAAAAHzjlsATWoc9-SXv8QYsx8nsYAap";

    private final RestTemplate restTemplate;

    @Autowired
    public ReCaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean validateCaptcha(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String response = request.getParameter("recaptcha_response_field");

        return validateCaptcha(remoteAddr, response);
    }


    @Override
    public boolean validateCaptcha(String clientRemoteAddress, String response) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", privateKey);
        params.add("response", response);
        params.add("remoteip", clientRemoteAddress);
        RecaptchaResponse recaptchaResponse = restTemplate.postForObject("https://www.google.com/recaptcha/api/siteverify", params, RecaptchaResponse.class);

        return recaptchaResponse.success;

    }

    public String renderToHtml() {
        ReCaptcha c = ReCaptchaFactory.newReCaptcha(publicKey, privateKey, false);
        return c.createRecaptchaHtml(null, null);
    }


    public static class RecaptchaResponse {

        public boolean success;

        @JsonProperty(value = "error-codes")
        public List<String> errorCodes;


    }
}
