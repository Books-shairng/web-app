package com.ninjabooks.controller;

import com.google.gson.Gson;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class UserController
{
    private final UserService userService;
    private final Gson gson;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ResponseEntity<Gson> createUser(@RequestBody User user) {
        userService.createUser(user);
        gson.toJson("User created", String.class);

        return new ResponseEntity<>(gson, HttpStatus.CREATED);
    }
}
