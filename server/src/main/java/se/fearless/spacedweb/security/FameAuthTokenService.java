package se.fearless.spacedweb.security;


import se.fearless.spacedweb.model.UserAccount;

import java.util.Optional;

public interface FameAuthTokenService {
	String createFameAuthToken(UserAccount userAccount, String feature);

	Optional<UserAccount> getUserFromAuthToken(String token, String feature);
}
