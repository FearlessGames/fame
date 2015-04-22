package se.fearless.spacedweb.web.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import se.fearless.spacedweb.services.AuthenticationSaltService;

@Controller
public class RequestSaltsController {
	private final AuthenticationSaltService authenticationSaltService;

	@Autowired
	public RequestSaltsController(AuthenticationSaltService authenticationSaltService) {
		this.authenticationSaltService = authenticationSaltService;
	}

	@RequestMapping(value = "/api/auth/requestSalts.html", method = RequestMethod.GET)
	public String requestSalts(Model model, String userName) {
		model.addAttribute("salts", authenticationSaltService.requestSalts(userName));
		return "requestSalts";
	}
}
