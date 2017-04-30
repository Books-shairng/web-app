package com.ninjabooks.controller;

import com.google.gson.Gson;
import com.ninjabooks.domain.User;
import com.ninjabooks.security.SpringSecurityUser;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

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
    private Gson gson;

    @Autowired
    public UserController(UserService userService, TokenUtils tokenUtils, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity<Gson> getAuthenticatedUser(@RequestParam HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        String email = tokenUtils.getUsernameFromToken(token);

        SpringSecurityUser user = (SpringSecurityUser) userDetailsService.loadUserByUsername(email);

        gson = new Gson();
        gson.toJson(user);

        return new ResponseEntity<>(gson, HttpStatus.FOUND);
//        gson = new Gson();
//        User userInfo = userService.baseUserInfo(email);
//
//        gson.toJson(userInfo.getId());
//        gson.toJson(userInfo.getEmail());
//        gson.toJson(userInfo.getName());
//
//        return new ResponseEntity<>(gson, HttpStatus.FOUND);
    }
}
