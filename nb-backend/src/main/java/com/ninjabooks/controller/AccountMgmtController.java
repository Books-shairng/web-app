package com.ninjabooks.controller;

import com.ninjabooks.error.exception.management.ManagementException;
import com.ninjabooks.json.message.MessageResponse;
import com.ninjabooks.service.rest.account.PasswordManagementService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@RestController
@RequestMapping(value = "/api/management/{userID}/")
public class AccountMgmtController
{
    private final PasswordManagementService passwordManagementService;

    @Autowired
    public AccountMgmtController(PasswordManagementService passwordManagementService) {
        this.passwordManagementService = passwordManagementService;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "password", method = RequestMethod.POST)
    public MessageResponse changePassword(@PathVariable(name = "userID") Long id,
                                          @RequestBody Map<String, String> requestData) throws ManagementException {
        passwordManagementService.change(id, requestData.get("password"));
        return new MessageResponse("Successfully change password");
    }
}
