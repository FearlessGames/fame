package se.fearless.spacedweb.services;

public interface PasswordResetService {
    public void requestAndSendResetToken(String email) throws NoSuchEmailException;

    public boolean isValidToken(String token);

    public void changePassword(String token, String newPassword) throws IllegalTokenException;
}
