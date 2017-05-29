package com.ninjabooks.configuration;

import org.hibernate.SessionFactory;
import org.hsqldb.util.DatabaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This configuration class contains all necessary beans to inject
 * depedencies.
 *
 * Use this config to make some <b>developing</b> things like:
 * unit testing
 * checking integration with conrtollers and etc
 *
 * For more information what's in - memory - db  look at
 * <a href="http://hsqldb.org/"> click me </a>
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Lazy
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.ninjabooks.dao"})
@Profile(value = {"dev", "test"})
public class HSQLConfig implements DBConnectConfig
{
    private final Environment environment;

    @Autowired
    public HSQLConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Override
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        String activeProfile =  environment.getActiveProfiles()[0];

        builder.scanPackages("com.ninjabooks.domain")
            .addProperties(hibernateProperties());

        if (activeProfile.equals("dev") && !activeProfile.isEmpty()) {
            builder.addProperties(importDataToDB());
            DatabaseManager.threadedDBM();
        }

        return builder.buildSessionFactory();
    }

    @Bean
    @Override
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:books-sharing;ifexists=false");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    @Override
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "create");
//        properties.put("hibernate.current_session_context_class", "thread");
        properties.put("hibernate.rollback", "false");
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
    @Override
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
