package com.ninjabooks.configuration;

import org.hibernate.SessionFactory;
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
 * @see SessionFactory in every class.
 *
 * Configuration is valid only with <b>production</b> database, to testing
 * and other thing use
 * @see HSQLConfig
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Lazy
@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:prod-hibernate.properties")
@ComponentScan(basePackages = {"com.ninjabooks.dao"})
@Profile(value = "prod")
public class MySQLConfig implements DBConnectConfig
{
    private final Environment environment;

    @Autowired
    public MySQLConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Override
    public SessionFactory sessionFactory() {
        LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSource());
        builder.scanPackages("com.ninjabooks.domain")
                .addProperties(hibernateProperties());
        return builder.buildSessionFactory();
    }

    @Bean
    @Override
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource("prod-hibernate.properties");
        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
        return dataSource;
    }

    @Bean
    @Override
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", "update");
//        properties.put("hibernate.current_session_context_class", "thread");
        properties.put("hibernate.rollback", "false");
        properties.put("hibernate.search.default.directory_provider",
            environment.getRequiredProperty("hibernate.search.default.directory_provider"));
        properties.put("hibernate.search.default.indexBase", environment.getRequiredProperty("hibernate.search.default.indexBase"));
        return properties;
    }

    @Bean
    @Override
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
