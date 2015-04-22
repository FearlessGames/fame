package se.fearless.spacedweb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.fearless.common.uuid.UUIDFactory;
import se.fearless.spacedweb.model.Salts;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationSaltServiceImpl implements AuthenticationSaltService {

	private final UUIDFactory uuidFactory;
	private final UserAccountDao userAccountDao;
	private Map<String, String> oneTimeSaltsByUsername = new HashMap<String, String>();


	@Autowired
	public AuthenticationSaltServiceImpl(UUIDFactory uuidFactory, UserAccountDao userAccountDao) {
		this.uuidFactory = uuidFactory;
		this.userAccountDao = userAccountDao;
	}

	@Override
    @Transactional
    public Salts requestSalts(String username) {
		String oneTimeSalt = uuidFactory.randomUUID().toString();
		oneTimeSaltsByUsername.put(username, oneTimeSalt);
		UserAccount userAccount = userAccountDao.findByUsername(username);

		if (userAccount == null || userAccount.getUserSalt() == null || userAccount.getUserSalt().isEmpty()) {
			throw new RuntimeException("Could not request salts");
		}

		return new Salts(userAccount.getUserSalt(), oneTimeSalt);
	}

	@Override
	public String getOneTimeSaltForUsername(String username) {
		return oneTimeSaltsByUsername.remove(username);
	}
}
