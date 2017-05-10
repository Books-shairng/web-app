package com.ninjabooks.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security class
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = {UserAuthService.class, EntryPointUnauthorizedHandler.class})
@EnableWebSecurity
public class WebSecuirtyConfig extends WebSecurityConfigurerAdapter
{
    private final UserDetailsService userAuthService;
    private final EntryPointUnauthorizedHandler unauthorizedHandler;
    private final TokenUtils tokenUtils;


    @Autowired
    public WebSecuirtyConfig(UserDetailsService userAuthService, EntryPointUnauthorizedHandler unauthorizedHandler, TokenUtils tokenUtils) {
        this.userAuthService = userAuthService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.tokenUtils = tokenUtils;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(userAuthService)
            .passwordEncoder(passwordEncoder());
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/**").permitAll()
            .antMatchers("/auth/**").permitAll()
            .anyRequest().authenticated();

        // Custom JWT based security filter
        http
            .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers().cacheControl();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() throws Exception {
        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter(userAuthService, tokenUtils);
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        return authenticationTokenFilter;
    }
}
