package com.ninjabooks;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Main app class
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class App
{
    /**
     * Start application from this point.
     * @param args nothing
     */

    public static void main(String[] args) {
        //Perhaps it will replace with super method from spring mvc depedency :)
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext();
    }
}
