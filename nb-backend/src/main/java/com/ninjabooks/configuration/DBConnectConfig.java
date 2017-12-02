package com.ninjabooks.configuration;

import javax.sql.DataSource;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hsqldb.util.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This configuration class contains all necessary beans to inject
 * Configuration is valid with <b>any</b> database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Lazy
@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:${spring.profiles.active}-hibernate.properties")
@ComponentScan(basePackages = {"com.ninjabooks.dao"})
@Profile(value = {"prod", "dev", "test"})
public class DBConnectConfig
{
    private final Environment environment;

    @Autowired
    public DBConnectConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        String activeProfile = environment.getActiveProfiles()[0];

        builder.scanPackages("com.ninjabooks.domain")
            .addProperties(hibernateProperties());

        if (activeProfile.equals("dev")) {
            builder.addProperties(importDataToDB());
            DatabaseManager.threadedDBM();
        }

        return builder.buildSessionFactory();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    public Properties hibernateProperties() {
//        properties.put("hibernate.current_session_context_class", "thread");
        Properties properties = new Properties();
        properties.put("hibernate.dialect",
            environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql",
            environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql",
            environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto",
            environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.rollback",
            environment.getRequiredProperty("hibernate.rollback"));
        properties.put("hibernate.search.default.directory_provider",
            environment.getRequiredProperty("hibernate.search.default.directory_provider"));

        return properties;
    }

    @Bean
    @Profile(value = "dev")
    public Properties importDataToDB() {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.import_files", "queries/import.sql");
        return properties;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
