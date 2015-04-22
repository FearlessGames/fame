package se.fearless.spacedweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import se.fearless.common.time.SystemTimeProvider;
import se.fearless.common.time.TimeProvider;
import se.fearless.common.uuid.UUIDFactory;
import se.fearless.common.uuid.UUIDFactoryImpl;

import java.util.Random;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${app.version:}")
    private String appVersion;

    @Value("${app.devMode:}")
    private String devMode;

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        boolean devMode = "true".equals(this.devMode);

        String location = devMode ? "file:///" + System.getProperty("user.dir") + "/frontend/src/" : "classpath:static/";
        Integer cachePeriod = devMode ? 0 : null;
        boolean useResourceCache = !devMode;
        String version = devMode ? "dev" : this.appVersion;

        AppCacheManifestTransformer appCacheTransformer = new AppCacheManifestTransformer();
        VersionResourceResolver versionResolver = new VersionResourceResolver()
                .addFixedVersionStrategy(version, "/**/*.js")
                .addContentVersionStrategy("/**");

        if(devMode) {
            versionResolver = versionResolver.addFixedVersionStrategy(version, "/**/*.map");
        }

        registry.addResourceHandler("/**")
                .addResourceLocations(location)
                .setCachePeriod(cachePeriod)
                .resourceChain(useResourceCache)
                .addResolver(versionResolver)
                .addTransformer(appCacheTransformer);

    }
}
