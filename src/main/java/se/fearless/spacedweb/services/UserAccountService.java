package se.fearless.spacedweb.services;

import se.fearless.spacedweb.model.UserAccount;

public interface UserAccountService {
    public boolean isAccountAuthorizedForFeature(UserAccount userAccount, String feature);

    /**
     *
     * @param accountName
     * @param providedHash should be sha512(bcrypt(username+password,usersalt)+onetimesalt)
     * @return
     */
    public UserAccount authenticate(String accountName, String providedHash);

    public void createAccount(String username, String password, String email) throws EmailOccupiedException, UsernameOccupiedException;
}
