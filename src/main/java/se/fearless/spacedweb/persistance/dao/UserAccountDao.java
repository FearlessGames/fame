package se.fearless.spacedweb.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import se.fearless.common.uuid.UUID;
import se.fearless.spacedweb.model.UserAccount;

public interface UserAccountDao extends JpaRepository<UserAccount, UUID> {
	public UserAccount findByUsername(String username);

	public UserAccount findByEmail(String email);
}
