package se.fearless.spacedweb;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

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

        if (devMode.equals("frontend")) {
            String frontEnd = "file:///" + System.getProperty("user.dir") + "/frontend/src/";
            registry.addResourceHandler("/**")
                    .addResourceLocations(frontEnd);

        } else if (devMode.equals("angular-frontend")) {
            String angularFrontEnd = "file:///" + System.getProperty("user.dir") + "/angular-frontend/";
            registry.addResourceHandler("/**")
                    .addResourceLocations(angularFrontEnd + "app/");
            registry.addResourceHandler("/bower_components/**")
                    .addResourceLocations(angularFrontEnd + "bower_components/");
        } else {
            registry.addResourceHandler("/**")
                    .addResourceLocations("classpath:static/");
        }

    }
}
