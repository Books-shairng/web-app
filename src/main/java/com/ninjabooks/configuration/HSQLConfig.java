package com.ninjabooks.configuration;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.ninjabooks.dao"})
@Profile(value = "dev")
public class HSQLConfig implements DBConnectConfig
{
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
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:mem:butterfly");
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
        properties.put("hibernate.hbm2ddl.auto", "update");
//        properties.put("hibernate.current_session_context_class", "thread");
        properties.put("hibernate.rollback", "false");
        return properties;
    }

    @Bean
    @Override
    public HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(sessionFactory());
    }
}
