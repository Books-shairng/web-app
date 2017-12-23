package com.ninjabooks.security.service.auth;

import com.ninjabooks.json.authentication.AuthenticationRequest;
import com.ninjabooks.security.user.SpringSecurityUser;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface AuthenticationService
{
    UserDetails authUser(AuthenticationRequest request);
    Optional<String> refreshToken(String header);
    SpringSecurityUser getAuthUser(String header);
}
