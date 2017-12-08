package com.ninjabooks;

import com.ninjabooks.configuration.AppConfig;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * Main app class
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class WebApp implements WebApplicationInitializer
{
    private final static Logger logger = LogManager.getLogger(WebApp.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("Application initilization: {}", servletContext.getServerInfo());

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        setProfile(applicationContext);
        applicationContext.register(AppConfig.class);

        servletContext.addListener(new ContextLoaderListener(applicationContext));

        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
        servlet.setAsyncSupported(true);

        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }

    private void setProfile(AnnotationConfigWebApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        try {
            environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:application.properties"));
            System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, environment.getActiveProfiles()[0]);
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
