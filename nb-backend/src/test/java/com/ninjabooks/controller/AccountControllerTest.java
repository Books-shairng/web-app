package com.ninjabooks.controller;

import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.error.handler.AccountControllerHandler;
import com.ninjabooks.security.service.auth.AuthenticationService;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.service.rest.account.AccountService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.FIRSTNAME;
import static com.ninjabooks.util.constants.DomainTestConstants.LASTNAME;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AccountControllerTest extends BaseUTController
{
    private static final String TOKEN = "Bearer top_sercret_token";
    private static final String URL = "/api/user";
    private static final String JSON =
        "{\"firstName\":\"" + FIRSTNAME + "\",\"lastName\":\"" + LASTNAME + "\"," +
        "\"email\":\"" + EMAIL + "\",\"password\":\"" + PLAIN_PASSWORD + "\"}";

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private AuthenticationService authenticationServiceMock;

    private AccountController sut;

    @Before
    public void setUp() throws Exception {
        sut = new AccountController(accountServiceMock, authenticationServiceMock);
        mockMvc(standaloneSetup(sut).setControllerAdvice(new AccountControllerHandler()));
    }

    @Test
    public void testCreateUserShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(URL)
            .withContent(JSON)
            .withStatus(CREATED)
            .build());

        verify(accountServiceMock, atLeastOnce()).createUser(any());
    }

    @Test
    public void testCreateUserWhichAlreadyExistShouldThrowsException() throws Exception {
        doThrow(UserAlreadyExistException.class).when(accountServiceMock).createUser(any());

        doPost(new HttpRequestBuilder(URL)
            .withContent(JSON)
            .withStatus(BAD_REQUEST)
            .build());

        verify(accountServiceMock, atLeastOnce()).createUser(any());
    }

    @Test
    public void testGetAuthentationShouldReturnUserInfo() throws Exception {
        SpringSecurityUser springUser = initSpringUser();
        when(authenticationServiceMock.getAuthUser(any())).thenReturn(springUser);

        doGet(new HttpRequestBuilder(URL)
            .withHeader("Authorization", TOKEN)
            .withStatus(FOUND)
            .build());

        verify(authenticationServiceMock, atLeastOnce()).getAuthUser(any());
    }

    private SpringSecurityUser initSpringUser() {
        SpringSecurityUser springSecurityUser = new SpringSecurityUser();
        springSecurityUser.setName(NAME);
        springSecurityUser.setEmail(EMAIL);
        return springSecurityUser;
    }
}
