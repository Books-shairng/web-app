package com.ninjabooks.security;

import com.ninjabooks.util.SecurityHeaderFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter
{
    private final static Logger logger = LogManager.getLogger(AuthenticationTokenFilter.class);

    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;
    private final SecurityHeaderFinder securityHeaderFinder;

    @Autowired
    public AuthenticationTokenFilter(TokenUtils tokenUtils, UserDetailsService userDetailsService, SecurityHeaderFinder securityHeaderFinder) {
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
        this.securityHeaderFinder = securityHeaderFinder;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String tokenHeader = "Authorization";

        String authToken = httpRequest.getHeader(tokenHeader);
        authToken = extractTokenIfExist(authToken);

        performAuthentication(httpRequest, authToken);

        chain.doFilter(req, res);
    }

    private void performAuthentication(HttpServletRequest httpRequest, String authToken) {
        String username = tokenUtils.getUsernameFromToken(authToken);

        logger.info("Checking authentication for user " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenUtils.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    private String extractTokenIfExist(String authToken) {
        if (authToken != null && securityHeaderFinder.hasSecurityPattern(authToken))
            return securityHeaderFinder.extractToken(authToken);

        return authToken;
    }
}
