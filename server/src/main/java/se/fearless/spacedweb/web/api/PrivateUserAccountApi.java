package se.fearless.spacedweb.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.fearless.spacedweb.security.FameAuthTokenService;
import se.fearless.spacedweb.services.EmailOccupiedException;
import se.fearless.spacedweb.services.UserAccountService;
import se.fearless.spacedweb.services.UsernameOccupiedException;

import java.util.Date;

@RestController
@RequestMapping(value = "/api/private/users")
public class PrivateUserAccountApi {
	private final UserAccountService userAccountService;
	private final FameAuthTokenService fameAuthTokenService;

	@Autowired
	public PrivateUserAccountApi(FameAuthTokenService fameAuthTokenService, UserAccountService userAccountService) {
		this.fameAuthTokenService = fameAuthTokenService;
		this.userAccountService = userAccountService;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody CreateUserDTO createUserDTO) {
		try {
			userAccountService.createAccount(createUserDTO.username, createUserDTO.password, createUserDTO.email);
		} catch (EmailOccupiedException | UsernameOccupiedException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{feature}/{fameAuthToken}", method = RequestMethod.GET)
	public ResponseEntity<UserAccountDTO> get(@PathVariable("feature") String feature, @PathVariable("fameAuthToken") String fameAuthToken) {
		return fameAuthTokenService.getUserFromAuthToken(fameAuthToken, feature)
				.map(userAccount -> new ResponseEntity<>(new UserAccountDTO(userAccount.getUsername(), userAccount.getEmail(), userAccount.getTimeOfRegistration()), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
	}


	private static class CreateUserDTO {
		public String username;
		public String email;
		public String password;
	}

	private static class UserAccountDTO {
		public String username;
		public String email;
		public Date timeOfRegistration;

		public UserAccountDTO(String username, String email, Date timeOfRegistration) {
			this.username = username;
			this.email = email;
			this.timeOfRegistration = timeOfRegistration;
		}
	}
}
