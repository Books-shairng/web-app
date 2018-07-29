package com.ninjabooks.controller;

import com.ninjabooks.security.utils.TokenUtils;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;
import com.ninjabooks.utils.TestDevice;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.FIRSTNAME;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.LASTNAME;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AccountControllerIT extends BaseITController
{
    private static final String URL = "/api/user";
    private static final String USER_CREATE_MESSAGE = "User was successfully created";
    private static final String SHORT_PASSWORD = "aa";
    private static final String JSON =
        "{\"firstName\":\"" + FIRSTNAME + "\",\"lastName\":\"" + LASTNAME + "\"," +
            "\"email\":\"" + EMAIL + "\",\"password\":\"" + PLAIN_PASSWORD + "\"}";

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void testCreateUserShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(URL)
            .withContent(JSON)
            .withStatus(CREATED)
            .build(), singletonMap("$.message", USER_CREATE_MESSAGE));
    }

    @Test
    public void testCreateUserWithoutMailFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.email").jsonString();
        callApiWithExpectedMessageAsResponse(json, "email field must be not empty");
    }

    @Test
    public void testCreateUserWithoutFirstNameFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.firstName").jsonString();
        callApiWithExpectedMessageAsResponse(json, "firstName field must be not empty");
    }

    @Test
    public void testCreateUserWithoutLastNameFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.lastName").jsonString();
        callApiWithExpectedMessageAsResponse(json, "lastName field must be not empty");
    }

    @Test
    public void testCreateUserWithoutPasswordFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.password").jsonString();
        callApiWithExpectedMessageAsResponse(json, "password field must be not empty");
    }

    @Test
    public void testCreateUserWithShortPasswordShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).set("$.password", SHORT_PASSWORD).jsonString();
        callApiWithExpectedMessageAsResponse(json, "password is too short, minimum length must equals: 8");
    }

    @Test
    public void testCreateUserWithMalformedEmailShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).set("$.email", NAME).jsonString();
        callApiWithExpectedMessageAsResponse(json, "email is not a well-formated");
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateUserWhichAlreadyExistShouldThrowsException() throws Exception {
        String expectedResponse = format("Username email: {0} already exist in database", EMAIL);
        callApiWithExpectedMessageAsResponse(JSON, expectedResponse);
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAuthentationShouldSucceed() throws Exception {
        Map<String, Object> expctedJson = ImmutableMap.of(
            "$.id", ID.intValue(),
            "$.firstName", FIRSTNAME,
            "$.lastName", LASTNAME,
            "$.email", EMAIL);

        doGet(new HttpRequestBuilder(URL)
            .withHeader("Authorization", generateToken())
            .withStatus(FOUND)
            .build(), expctedJson);
    }

    private String generateToken() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(EMAIL);
        String token = tokenUtils.generateToken(userDetails, TestDevice.createDevice());
        return "Bearer " + token;
    }

    private void callApiWithExpectedMessageAsResponse(String json, String message) throws Exception {
        doPost(new HttpRequestBuilder(URL)
            .withContent(json)
            .withStatus(BAD_REQUEST)
            .build(), singletonMap("$.message", message));
    }
}
