package com.ninjabooks.security.service.auth;

import com.ninjabooks.json.authentication.AuthenticationRequest;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.security.utils.SecurityHeaderUtils;
import com.ninjabooks.security.utils.TokenUtils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService
{
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager,
                                     UserDetailsService userDetailsService,
                                     TokenUtils tokenUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @Override
    public UserDetails authUser(AuthenticationRequest request) {
        performAuthentication(request);
        return userDetailsService.loadUserByUsername(request.getEmail());
    }

    @Override
    public Optional<String> refreshToken(String header) {
        String token = SecurityHeaderUtils.extractTokenFromHeader(header);
        SpringSecurityUser user = extractUser(token);

        return tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset()) ?
            Optional.ofNullable(tokenUtils.refreshToken(token)) :
            Optional.empty();
    }

    @Override
    public SpringSecurityUser getAuthUser(String header) {
        String token = SecurityHeaderUtils.extractTokenFromHeader(header);
        return extractUser(token);
    }

    private SpringSecurityUser extractUser(String token) {
        String extractedEmail = tokenUtils.getUsernameFromToken(token);
        return (SpringSecurityUser) userDetailsService.loadUserByUsername(extractedEmail);
    }

    private void performAuthentication(AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
