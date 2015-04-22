package se.fearless.spacedweb.web.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DummyController {
	@RequestMapping(value = "/dummy.html", method = RequestMethod.GET)
	public String get() {
		return "accountCreated";
	}

}