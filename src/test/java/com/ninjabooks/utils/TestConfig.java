package com.ninjabooks.utils;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Annotations based configuration class for junit class
 * @see com.ninjabooks.dao
 *
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
@ComponentScan(basePackages = "com.ninjabooks.dao")
@Configuration
public class TestConfig
{
    
}
