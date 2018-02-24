package com.ninjabooks.controller;

import com.ninjabooks.error.exception.management.ManagementException;
import com.ninjabooks.error.handler.AccountMgmtControllerHandler;
import com.ninjabooks.service.rest.account.PasswordManagementService;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AccountMngtControllerTest
{
    private static final String API_URL_REQUEST = "/api/management/{userID}/";
    private static final String JSON = "" +
        "{" +
            "\"password\" : " + PLAIN_PASSWORD +
        "}";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private PasswordManagementService passwordManagementServiceMock;

    private MockMvc mockMvc;
    private AccountMgmtController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new AccountMgmtController(passwordManagementServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(new AccountMgmtControllerHandler())
            .build();
    }

    @Test
    public void testChangePasswordShouldReturnStatusOk() throws Exception {
        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .content(JSON)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testChangePasswordShouldSucceedAndReturnExpectedMessage() throws Exception {
        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .content(JSON)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.message").value("Successfully change password"));
    }

    @Test
    public void testChangePasswordShouldThrowsExceptionWhenPasswordNotUnique() throws Exception {
        doThrow(ManagementException.class).when(passwordManagementServiceMock).change(anyLong(), anyString());

        mockMvc.perform(post(API_URL_REQUEST + "password", ID)
            .content(JSON)
            .contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(passwordManagementServiceMock, atLeastOnce()).change(anyLong(), anyString());
    }
}
