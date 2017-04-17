package com.ninjabooks.configuration;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * This interface describe abstract layer of connection to serveral database types.
 *
 * @see HSQLConfig configuration for testing/develop database
 * @see MySQLConfig configuration for production database
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface DBConnectConfig
{
    SessionFactory sessionFactory();
    DataSource dataSource();
    Properties hibernateProperties();
    HibernateTransactionManager transactionManager();
}
