package com.ninjabooks.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Configuration
@ActiveProfiles(value = "test")
@ComponentScan(basePackages = "com.ninjabooks")
public class AppITConfig
{
}

