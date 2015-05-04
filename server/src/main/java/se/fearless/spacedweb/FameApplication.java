package se.fearless.spacedweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import se.fearless.spacedweb.services.EmailOccupiedException;
import se.fearless.spacedweb.services.UserAccountService;
import se.fearless.spacedweb.services.UsernameOccupiedException;

@SpringBootApplication
public class FameApplication extends SpringBootServletInitializer {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) throws EmailOccupiedException, UsernameOccupiedException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(FameApplication.class, args);
        UserAccountService userAccountService = applicationContext.getBean(UserAccountService.class);
        userAccountService.createAccount("foobar", "12345", "foo@bar.com");
    }


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FameApplication.class);
    }
}
