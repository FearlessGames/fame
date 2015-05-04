package se.fearless.spacedweb.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReCaptchaService implements CaptchaService {

    private final RestTemplate restTemplate;
    private final String publicKey;
    private final String privateKey;

    @Autowired
    public ReCaptchaService(RestTemplate restTemplate, @Qualifier("recaptcha.publicKey") String publicKey, @Qualifier("recaptcha.privateKey") String
            privateKey) {
        this.restTemplate = restTemplate;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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
