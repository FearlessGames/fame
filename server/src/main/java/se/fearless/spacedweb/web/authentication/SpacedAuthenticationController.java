package se.fearless.spacedweb.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.services.UserAccountService;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Controller
public class SpacedAuthenticationController extends AuthenticationController {
	private static final String SPACED_FEATURE = "spaced";

	@Autowired
	public SpacedAuthenticationController(UserAccountService userAccountService, @Qualifier("spacedServiceKey") String expectedServiceKey) {
		super(userAccountService, expectedServiceKey);
	}

	@RequestMapping(value = "/api/auth/spaced.html", method = RequestMethod.GET)
	public String authenticateSpaced(HttpServletResponse response, Model model, String userName, String hash, String serviceKey) {
		authService(serviceKey);
		Optional<UserAccount> userAccount = userAccountService.authenticate(userName, hash);
		authorize(response, model, SPACED_FEATURE, userAccount.get());
		return "authSpaced";
	}

}
