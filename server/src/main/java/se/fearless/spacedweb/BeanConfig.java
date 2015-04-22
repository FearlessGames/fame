package se.fearless.spacedweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import se.fearless.common.time.SystemTimeProvider;
import se.fearless.common.time.TimeProvider;
import se.fearless.common.uuid.UUIDFactory;
import se.fearless.common.uuid.UUIDFactoryImpl;

import java.util.Random;

@Configuration
public class BeanConfig {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${auth.servicekey.spaced}")
	private String spacedServiceKey;

	@Value("${auth.servicekey.forum}")
	private String forumServiceKey;


	@Value("${mailserver}")
	private String mailServer;

	@Bean
	@Qualifier("spacedServiceKey")
	public String getSpacedServiceKey() {
		return spacedServiceKey;
	}

	@Bean
	@Qualifier("forumServiceKey")
	public String forumServiceKey() {
		return forumServiceKey;
	}

	@Bean
	public TimeProvider timeProvider() {
		return new SystemTimeProvider();
	}

	@Bean
	public Random numberGenerator() {
		return new Random();
	}


	@Bean
	@Autowired
	public UUIDFactory uuidFactory(TimeProvider timeProvider, Random random) {
		return new UUIDFactoryImpl(timeProvider, random);
	}

	@Bean
	public MailSender mailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(mailServer);
		return javaMailSender;
	}

}
