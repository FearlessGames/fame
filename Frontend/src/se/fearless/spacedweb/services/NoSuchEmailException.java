package se.fearless.spacedweb.services;

public class NoSuchEmailException extends Exception {
    public NoSuchEmailException(String email) {
        super("no such email: " + email);
    }
}
