package se.fearless.spacedweb.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.fearless.common.security.BCrypter;
import se.fearless.common.uuid.UUIDFactory;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;
import se.fearless.spacedweb.services.mail.ResetPasswordMail;

import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {
	private final UUIDFactory uuidFactory;
	private final UserAccountDao userAccountDao;
	private final MailSender mailSender;
	private final Map<String, String> usernameByToken = new HashMap<String, String>();
	private static final String BASE_URL_FOR_RESET = "http://fame.fearlessgames.se/changePassword.html?token=";
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	public PasswordResetServiceImpl(UUIDFactory uuidFactory, UserAccountDao userAccountDao, MailSender mailSender) {
		this.uuidFactory = uuidFactory;
		this.userAccountDao = userAccountDao;
		this.mailSender = mailSender;
	}

	@Override
	public void requestAndSendResetToken(String email) throws NoSuchEmailException {
		UserAccount userAccount = userAccountDao.findByEmail(email);
		if (userAccount == null) {
			throw new NoSuchEmailException(email);
		}
		String tokenString = uuidFactory.randomUUID().toString();
		log.debug("created token {} for pwd reset", tokenString);

		usernameByToken.put(tokenString, userAccount.getUsername());
		mailSender.send(new ResetPasswordMail(email, tokenString, BASE_URL_FOR_RESET));
	}

	@Override
	public boolean isValidToken(String token) {
		return usernameByToken.containsKey(token);
	}

	@Override
	@Transactional
	public void changePassword(String token, String newPassword) throws IllegalTokenException {
		String username = usernameByToken.remove(token);
		if (username == null) {
			log.warn("Token not found when resetting pwd: {}", token);
			throw new IllegalTokenException(token);
		}
		log.debug("resetting password for user: {}", username);
		UserAccount userAccount = userAccountDao.findByUsername(username);
		String newUserSalt = BCrypter.generateBCryptSalt();
		String pwdHash = BCrypter.bcrypt(username + newPassword, newUserSalt);
		userAccount.setPassword(pwdHash);
		userAccount.setUserSalt(newUserSalt);
		userAccountDao.persist(userAccount);
	}
}
