package com.ninjabooks.controller;

import com.ninjabooks.domain.User;
import com.ninjabooks.json.user.UserRequest;
import com.ninjabooks.json.user.UserResponse;
import com.ninjabooks.security.SpringSecurityUser;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.service.UserService;
import com.ninjabooks.util.SecurityHeaderFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class UserController
{
    private final UserService userService;
    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;
    private final SecurityHeaderFinder securityHeaderFinder;

    @Autowired
    public UserController(UserService userService, TokenUtils tokenUtils, UserDetailsService userDetailsService, SecurityHeaderFinder securityHeaderFinder) {
        this.userService = userService;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
        this.securityHeaderFinder = securityHeaderFinder;
    }

    /**
     * Create new user in system.
     *
     * @param userRequest - received from request
     * @return http response with status 201
     */

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        User userFromRequest = new User(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword());

        userService.createUser(userFromRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * After proper authorization return base info about user like his id, mail and name
     *
     * @param httpServletRequest - http request which contains authorization header with
     *                           jwt token
     * @return user info as json (content: id, password, email)
     */

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader("Authorization");
        String token = null;

        if (securityHeaderFinder.hasSecurityPattern(header))
            token = securityHeaderFinder.extractToken(header);

        String email = tokenUtils.getUsernameFromToken(token);

        SpringSecurityUser user = (SpringSecurityUser) userDetailsService.loadUserByUsername(email);

        return new ResponseEntity<>(new UserResponse(user), HttpStatus.FOUND);
    }
}
