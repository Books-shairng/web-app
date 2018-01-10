package com.ninjabooks.config;

import com.ninjabooks.configuration.TestAppContextInitializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Documented
@Inherited
@WebAppConfiguration
@ContextConfiguration(
    loader = AnnotationConfigWebContextLoader.class,
    classes = AppITConfig.class,
    initializers = TestAppContextInitializer.class)
@ActiveProfiles("test")
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface IntegrationTest
{
}
