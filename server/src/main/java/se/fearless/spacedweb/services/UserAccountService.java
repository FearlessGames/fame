package se.fearless.spacedweb.services;

import se.fearless.spacedweb.model.UserAccount;

import java.util.Optional;

public interface UserAccountService {
	public boolean isAccountAuthorizedForFeature(UserAccount userAccount, String feature);

	/**
	 * @param accountName
	 * @param providedHash should be sha512(bcrypt(username+password,usersalt)+onetimesalt)
	 * @return
	 */
	Optional<UserAccount> authenticate(String accountName, String providedHash);

	void createAccount(String username, String password, String email) throws EmailOccupiedException, UsernameOccupiedException;
}
