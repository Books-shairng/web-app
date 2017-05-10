package com.ninjabooks.controller;

import com.ninjabooks.domain.User;
import com.ninjabooks.security.SpringSecurityUser;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.service.UserService;
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

    @Autowired
    public UserController(UserService userService, TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Create new user in system.
     *
     * @param user - received from request
     * @return http response with status 201
     */

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.createUser(user);
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
        String token = httpServletRequest.getHeader("Authorization");
        String email = tokenUtils.getUsernameFromToken(token);

        SpringSecurityUser user = (SpringSecurityUser) userDetailsService.loadUserByUsername(email);

        return new ResponseEntity<>(user, HttpStatus.FOUND);
    }
}
