package com.ninjabooks.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class TestAppContextInitializer implements ApplicationContextInitializer<GenericApplicationContext>
{
    @Override
    public void initialize(GenericApplicationContext context) {
        context.getEnvironment().getSystemProperties()
            .put(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
    }
}
