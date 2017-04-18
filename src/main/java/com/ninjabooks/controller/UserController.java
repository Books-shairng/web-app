package com.ninjabooks.controller;

import com.ninjabooks.domain.User;
import com.ninjabooks.service.UserService;
import com.ninjabooks.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.PUT)
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);

        return user.toString();
    }
}
