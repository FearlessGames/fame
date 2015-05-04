package se.fearless.spacedweb.services;

public interface CaptchaService {
    boolean validateCaptcha(String clientRemoteAddress, String response);
}
