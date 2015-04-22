package se.fearless.spacedweb.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.fearless.spacedweb.model.UserAccount;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FameAuthTokenServiceImpl implements FameAuthTokenService {
	private final String password = "$46^$et:N`.`G#r?";

	private final Map<String, byte[]> featureKeyMap = new ConcurrentHashMap<>();

	private final ObjectMapper objectMapper;


	@Autowired
	public FameAuthTokenServiceImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public String createFameAuthToken(UserAccount userAccount, String feature) {
		try {

			//todo: we should have some other representation then the raw userAccount in the token

			byte[] bytes = objectMapper.writeValueAsBytes(userAccount);
			byte[] encrypt = encrypt(getKey(feature), bytes);
			return Base64.getEncoder().encodeToString(encrypt);

		} catch (Exception e) {
			throw new RuntimeException("Failed to serialize");
		}
	}

	@Override
	public Optional<UserAccount> getUserFromAuthToken(String token, String feature) {
		try {
			byte[] decode = Base64.getDecoder().decode(token);

			byte[] decryptedData = decrypt(getKey(feature), decode);
			UserAccount userAccount = objectMapper.readValue(decryptedData, UserAccount.class);
			return Optional.of(userAccount);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	private byte[] decrypt(byte[] key, byte[] encrypted) throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		return cipher.doFinal(encrypted);
	}

	private byte[] encrypt(byte[] key, byte[] clear) throws Exception {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return cipher.doFinal(clear);
	}


	private byte[] getKey(String feature) throws NoSuchAlgorithmException {
		byte[] key = featureKeyMap.get(feature);
		if (key == null) {
			String seed = password + "." + feature;

			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			sr.setSeed(seed.getBytes());
			keyGenerator.init(128, sr);
			SecretKey secretKey = keyGenerator.generateKey();
			key = secretKey.getEncoded();

			featureKeyMap.put(feature, key);
		}

		return key;
	}
}
