package se.fearless.spacedweb;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConfiguration {
	@Value("${jdbc.driverClassName}")
	private String driverClassName;

	@Value("${jdbc.hibernateDialect}")
	private String hibernateDialect;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;


	public String getDriverClassName() {
		return driverClassName;
	}

	public String getHibernateDialect() {
		return hibernateDialect;
	}

	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
