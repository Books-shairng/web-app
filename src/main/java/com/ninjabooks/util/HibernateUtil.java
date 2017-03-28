package com.ninjabooks.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
public class HibernateUtil
{
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
    private static final Logger logger = LogManager.getLogger(HibernateUtil.class);

    private static SessionFactory buildSessionFactory() {
        logger.info("Build session factory");
        try {
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory(new StandardServiceRegistryBuilder().build());
        } catch (Throwable e) {
            logger.error("Initial SessionFactory creation failed" + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
}
