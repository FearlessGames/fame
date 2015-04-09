package se.fearless.spacedweb.persistance.dao;

import se.fearless.spacedweb.model.UserAccount;

public interface UserAccountDao extends Dao<UserAccount> {
	public UserAccount findByUsername(String username);

	public UserAccount findByEmail(String email);
}
