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

@Controller
public class ForumAuthenticationController extends AuthenticationController {
	private static final String FEATURE = "forum";

	@Autowired
	public ForumAuthenticationController(UserAccountService userAccountService, @Qualifier("forumServiceKey") String expectedServiceKey) {
		super(userAccountService, expectedServiceKey);
	}

	@RequestMapping(value = "/api/auth/forum.html", method = RequestMethod.GET)
	public String authenticateSpaced(HttpServletResponse response, Model model, String userName, String hash, String serviceKey) {
		authService(serviceKey);
		UserAccount userAccount = userAccountService.authenticate(userName, hash);
		authorize(response, model, FEATURE, userAccount);
		return "authForum";
	}

}
