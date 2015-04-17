package se.fearless.spacedweb;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class HibernateProperties extends Properties {

	@Autowired
	public HibernateProperties(DatabaseConfiguration configuration) {
		setProperty("hibernate.dialect", configuration.getHibernateDialect());
		setProperty("hibernate.connection.driver_class", configuration.getDriverClassName());
		setProperty("hibernate.hbm2ddl.auto", "validate");
		setProperty("hibernate.show_sql", "true");
		setProperty("hibernate.hbm2ddl.auto", "update");
	}
}
