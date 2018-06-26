package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class AccountMgmtControllerIT extends BaseITController
{
    private static final String API_URL_REQUEST = "/api/management/{userID}/";
    private static final String RANDOM_PASWORD = RandomStringUtils.random(1);

    @Test
    public void testChangePasswordShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(API_URL_REQUEST + "password")
            .withUrlVars(ID)
            .withContent(generateJson(RANDOM_PASWORD))
            .build(), singletonMap("$.message", "Successfully change password"));
    }

    @Test
    public void testChangePasswordWithNotUniquePasswordShouldFail() throws Exception {
        doPost(new HttpRequestBuilder(API_URL_REQUEST + "password")
            .withUrlVars(ID)
            .withContent(generateJson(PLAIN_PASSWORD))
            .withStatus(BAD_REQUEST)
            .build(), singletonMap("$.message", "New password is not unique"));
    }

    private String generateJson(String password) {
        return format("{\"password\":\"%s\"}", password);
    }
}
