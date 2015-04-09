package se.fearless.spacedweb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;
import se.fearlessgames.common.security.BCrypter;
import se.fearlessgames.common.security.Digester;
import se.fearlessgames.common.uuid.UUID;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserAccountServiceImpl implements UserAccountService {
	private final UserAccountDao userAccountDao;
	private final Digester digester;
	private final AuthenticationSaltService authenticationSaltService;
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	public UserAccountServiceImpl(UserAccountDao userAccountDao, Digester digester, AuthenticationSaltService authenticationSaltService) {
		this.userAccountDao = userAccountDao;
		this.digester = digester;
		this.authenticationSaltService = authenticationSaltService;
	}

	@Override
	public boolean isAccountAuthorizedForFeature(UserAccount userAccount, String feature) {
		return userAccount.hasFeature(feature);
	}

	private List<String> getDefaultFeatures() {
		List<String> defaultFeatures = new ArrayList<String>();
		defaultFeatures.add("spaced");
		defaultFeatures.add("forum");
		return defaultFeatures;
	}


	@Override
	@Transactional
	public UserAccount authenticate(String username, String providedHash) {
		UserAccount userAccount = userAccountDao.findByUsername(username);
		String actualBCryptHash = userAccount.getPassword();  //pwd in db is bcrypt(username+password, usersalt). //provided hash is sha512(bcrypt(username+password, usersalt)+onetimesalt)
		String oneTimeSalt = authenticationSaltService.getOneTimeSaltForUsername(username);

		if (oneTimeSalt == null) {
			throw new RuntimeException("Could not find one time salt for username " + username);
		}

		String serverHash = digester.sha512Hex(actualBCryptHash + oneTimeSalt);
		return serverHash.equals(providedHash) ? userAccount : null;
	}

	@Override
	@Transactional
	public void createAccount(String username, String password, String email) throws EmailOccupiedException, UsernameOccupiedException {
		if (userAccountDao.findByEmail(email) != null) {
			throw new EmailOccupiedException();
		}

		if (userAccountDao.findByUsername(username) != null) {
			throw new UsernameOccupiedException();
		}

		String userSalt = BCrypter.generateBCryptSalt();
		String userHash = BCrypter.bcrypt(username + password, userSalt);

		byte[] bytes = digester.md5(username);
		UUID accountPk = UUID.nameUUIDFromBytes(bytes);
		UserAccount userAccount = new UserAccount(accountPk, username, userHash, userSalt, email, getDefaultFeatures());


		userAccountDao.persist(userAccount);

	}
}


