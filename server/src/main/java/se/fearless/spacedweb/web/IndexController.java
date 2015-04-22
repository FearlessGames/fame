package se.fearless.spacedweb.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class IndexController {

	@RequestMapping("/")
	public void hello(HttpServletResponse response) throws IOException {
		response.sendRedirect("index.html");
	}
}
