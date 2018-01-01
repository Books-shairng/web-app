package com.ninjabooks.security.filter;

import com.ninjabooks.security.utils.SecurityHeaderUtils;
import com.ninjabooks.security.utils.TokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter
{
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationTokenFilter(TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        String authToken = httpRequest.getHeader(AUTHORIZATION_HEADER);
        authToken = Objects.nonNull(authToken) ? SecurityHeaderUtils.extractTokenFromHeader(authToken) : null;
        performAuthentication(httpRequest, authToken);
        chain.doFilter(req, res);
    }

    private void performAuthentication(HttpServletRequest httpRequest, String authToken) {
        String username = tokenUtils.getUsernameFromToken(authToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenUtils.isValid(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }
}
