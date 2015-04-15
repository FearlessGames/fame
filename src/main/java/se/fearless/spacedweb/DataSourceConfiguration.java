package se.fearless.spacedweb;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {

	@Bean
	@Autowired
	public DataSource getDataSource(DatabaseConfiguration config) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(config.getDriverClassName());
		dataSource.setUrl(config.getUrl());
		dataSource.setUsername(config.getUsername());
		dataSource.setPassword(config.getPassword());
		return dataSource;
	}

	@Bean
	@Autowired
	public LocalSessionFactoryBean getSessionFactoryBean(DataSource datasource, HibernateProperties properties) {
		LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
		factory.setDataSource(datasource);
		factory.setHibernateProperties(properties);
		factory.setPackagesToScan(new String[]{"se.fearless.spacedweb.model"});
		return factory;
	}

	@Bean
	@Autowired
    public HibernateTransactionManager txManager(SessionFactory factory) {
        return new HibernateTransactionManager(factory);
	}


}
