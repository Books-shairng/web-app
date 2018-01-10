package com.ninjabooks.config;

import javax.sql.DataSource;

import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

/**
 * This is base class which is <bold>requried</bold> to proper run integration tests.
 *
 * @author Piotr 'pitrecki' Nowak
 * since 1.0
 */
@IntegrationTest
public abstract class AbstractBaseIT
{
    @Autowired
    private ApplicationContext applicationContext;

    private static final String CLEAN_DB_AND_RESET_ID_SCRIPT =
        "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK ;";

    @After
    public void tearDown() throws Exception {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setScripts(new ByteArrayResource(CLEAN_DB_AND_RESET_ID_SCRIPT.getBytes()));
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        populator.execute(dataSource);
    }
}
