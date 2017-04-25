package com.ninjabooks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 *
 * Main security class
 *
 */
@Configuration
@ComponentScan(basePackageClasses = UserAuthService.class)
@EnableWebSecurity
public class WebSecuirtyConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userAuthService;

    @Autowired
    public WebSecuirtyConfig(UserDetailsService userAuthService) {
        this.userAuthService = userAuthService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//            .anyRequest().permitAll()
//            .and()
//            .formLogin().loginPage("/login")
//            .usernameParameter("email")
//            .passwordParameter("password")
//            .and()
//            .logout().permitAll()
//            .and()
//            .csrf();

        http
            .exceptionHandling()
            .and()
            .authorizeRequests()
            .antMatchers("/", "/registration").anonymous()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/")
            .loginProcessingUrl("login")
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/notifications")
            .and()
            .logout().logoutSuccessUrl("/")
            .and()
            .csrf();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
