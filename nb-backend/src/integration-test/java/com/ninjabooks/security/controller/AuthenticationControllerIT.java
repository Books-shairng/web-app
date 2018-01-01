package com.ninjabooks.security.controller;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.security.utils.TokenUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.utils.TestDevice;

import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationControllerIT
{
    private final static String JSON_REQUEST =
        "{" +
            "\"email\" : \"" + DomainTestConstants.EMAIL + "\"," +
            "\"password\" : \"" + DomainTestConstants.PASSWORD + "\"" +
        "}";
    private static final int EXPECTED_SIZE = 1;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Sql(value = "classpath:it_auth_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAuthenticationWithCorrectDataShouldReturnStatus200() throws Exception {
        mockMvc.perform(post("/api/auth")
            .content(JSON_REQUEST)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:it_auth_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAuthenticationWithCorrectDataShouldReturnStatusTokenWithExpectedSize() throws Exception {
        mockMvc.perform(post("/api/auth")
            .content(JSON_REQUEST)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(jsonPath("$.token").isNotEmpty())
            .andExpect(jsonPath("$.length()").value(EXPECTED_SIZE));
    }

    @Test
    public void testAuthenticationWithIncorrectDataShouldFailed() throws Exception {
        mockMvc.perform(post("/api/auth")
            .content(JSON_REQUEST)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    @Sql(value = "classpath:it_auth_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRefreshTokenShouldReturnNewToken() throws Exception {
        String token = generateToken();

        MvcResult authorization = mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", token))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andReturn();

        String json = authorization.getResponse().getContentAsString();
        String actual = JsonPath.read(json, "$.token");

        assertThat(actual).isNotEqualTo(token);
    }

    @Test
    @Ignore("Test is disable beacuse token is fresh")
    @Sql(value = "classpath:it_auth_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRefreshTokenShouldReturnBadRequestWhenUnableToRefresh() throws Exception {
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRlZUBleG1hcGxlLmNvbSIsImF1ZGllbmNlIjoidW5rbm93biI" +
            "sImNyZWF0ZWQiOnsiZGF5T2ZNb250aCI6OCwiZGF5T2ZXZWVrIjoiV0VETkVTREFZIiwibW9udGgiOiJOT1ZFTUJFUiIsInllYXIiOjIwM" +
            "TcsImRheU9mWWVhciI6MzEyLCJtb250aFZhbHVlIjoxMSwiaG91ciI6MTksIm1pbnV0ZSI6MTAsIm5hbm8iOjQ0MDAwMDAwMCwic2Vjb25" +
            "kIjo1OSwiY2hyb25vbG9neSI6eyJpZCI6IklTTyIsImNhbGVuZGFyVHlwZSI6Imlzbzg2MDEifX0sImV4cCI6MTUxMDc2OTQ1OX0.Oyuwa" +
            "sg9CSWWZFqwW3xYjXTJZUcl5ZgvbmWYUCWtE0ZVFMi7hU3PtG6Q40XXNLKxV4p8Xku2GznrTEuUZNoocg";

        mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", token))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value("Failed to refresh the token"));
    }

    private String generateToken() {
        UserDetails userDetails = userDetailsService.loadUserByUsername(DomainTestConstants.EMAIL);
        String token = tokenUtils.generateToken(userDetails, TestDevice.createDevice());

        return "Bearer " + token;
    }
}
