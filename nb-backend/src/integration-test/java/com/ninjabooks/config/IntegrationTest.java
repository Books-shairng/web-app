package com.ninjabooks.config;

import com.ninjabooks.configuration.TestAppContextInitializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.*;

import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

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
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Target(value = ElementType.TYPE)
public @interface IntegrationTest
{
}
