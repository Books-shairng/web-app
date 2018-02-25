package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class AccountMgmtControllerIT extends AbstractBaseIT
{
    private static final String API_URL_REQUEST = "/api/management/{userID}/";
    private static final String UNIQUE_PASSWORD = "RaNdOm!@#123";

    @Autowired
    private WebApplicationContext wac;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testChangePasswordShouldSuccedAndReturnExpectedMessage() throws Exception {
        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(generateJson(UNIQUE_PASSWORD)))
            .andDo(print())
            .andExpect(jsonPath("$.message").value("Successfully change password"));
    }

    @Test
    public void testChangePasswordShouldSucceedAndReturnStatusOK() throws Exception {
        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(generateJson(UNIQUE_PASSWORD)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testChangePasswordWithNotUniquePasswordShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(generateJson(PLAIN_PASSWORD)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePasswordWithNotUniquePasswordShouldReturnExpectedErrorMessage() throws Exception {
        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(generateJson(PLAIN_PASSWORD)))
            .andDo(print())
            .andExpect(jsonPath("$.message").value("New password is not unique"));
    }

    private String generateJson(String password) {
        return
            "{" +
                "\"password\":" +"\"" + password + "\"" +
            "}";
    }
}
