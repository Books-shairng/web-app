package com.ninjabooks.controller;

import com.ninjabooks.error.exception.management.ManagementException;
import com.ninjabooks.error.handler.AccountMgmtControllerHandler;
import com.ninjabooks.service.rest.account.PasswordManagementService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AccountMgmtControllerTest extends BaseUTController
{
    private static final String API_URL_REQUEST = "/api/management/{userID}/";
    private static final String JSON = "{\"password\" : \"" + PLAIN_PASSWORD + "\"}";

    @Mock
    private PasswordManagementService passwordManagementServiceMock;

    private AccountMgmtController sut;

    @Before
    public void setUp() throws Exception {
        sut = new AccountMgmtController(passwordManagementServiceMock);
        mockMvc(standaloneSetup(sut).setControllerAdvice(new AccountMgmtControllerHandler()));
    }

    @Test
    public void testChangePasswordShouldReturnStatusOk() throws Exception {
        doPost(new HttpRequestBuilder(API_URL_REQUEST + "password")
            .withUrlVars(ID)
            .withContent(JSON)
            .build());
    }

    @Test
    public void testChangePasswordShouldSucceedAndReturnExpectedMessage() throws Exception {
        Map<String, Object> json = Collections.singletonMap("$.message", "Successfully change password");
        doPost(new HttpRequestBuilder(API_URL_REQUEST + "password")
            .withUrlVars(ID)
            .withContent(JSON)
            .build(), json);
    }

    @Test
    public void testChangePasswordShouldThrowsExceptionWhenPasswordNotUnique() throws Exception {
        doThrow(ManagementException.class).when(passwordManagementServiceMock).change(anyLong(), anyString());

        doPost(new HttpRequestBuilder(API_URL_REQUEST + "password")
            .withUrlVars(ID)
            .withContent(JSON)
            .withStatus(BAD_REQUEST)
            .build());

        verify(passwordManagementServiceMock, atLeastOnce()).change(anyLong(), anyString());
    }
}
