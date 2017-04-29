package com.ninjabooks.controller;

import com.google.gson.Gson;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class UserController
{
    private final UserService userService;
    private Gson gson;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity<Gson> basUserInfo(@RequestParam Principal email) {
        gson = new Gson();
        User userInfo = userService.baseUserInfo(email);

        gson.toJson(userInfo.getId());
        gson.toJson(userInfo.getEmail());
        gson.toJson(userInfo.getName());

        return new ResponseEntity<>(gson, HttpStatus.FOUND);
    }
}
