package com.ninjabooks.utils;

import com.ninjabooks.configuration.HibernateConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Annotations based com.ninjabooks.configuration class for junit class
 * @see com.ninjabooks.dao
 *
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
@ComponentScan(basePackageClasses = HibernateConfig.class)
@Configuration
public class TestConfig
{
    
}
