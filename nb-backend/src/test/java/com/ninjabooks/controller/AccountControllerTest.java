package com.ninjabooks.controller;

import com.ninjabooks.domain.User;
import com.ninjabooks.error.handler.AccountControllerHandler;
import com.ninjabooks.error.user.UserAlreadyExistException;
import com.ninjabooks.security.SpringSecurityUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.service.rest.account.AccountService;
import com.ninjabooks.util.SecurityHeaderFinder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AccountControllerTest
{
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Dee";
    private static final String EMAIL = "john.dee@exmaple.com";
    private static final String PASSWORD = "Johny!Dee123";
    private static final String NAME = FIRST_NAME + " " + LAST_NAME;

    private static final String JSON =
        "{" +
            "\"firstName\":\"" + FIRST_NAME + "\"," +
            "\"lastName\":\"" + LAST_NAME + "\"," +
            "\"email\":\"" + EMAIL + "\"," +
            "\"password\":\"" + PASSWORD + "\"}" +
            "}";

    @Mock
    private AccountService accountServiceMock;

    @Mock
    private TokenUtils tokenUtilsMock;

    @Mock
    private UserDetailsService userDetailsServiceMock;

    @Mock
    private SecurityHeaderFinder securityHeaderFinder;

    private MockMvc mockMvc;
    private AccountController sut;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.sut = new AccountController(accountServiceMock, tokenUtilsMock, userDetailsServiceMock,
            securityHeaderFinder);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .setControllerAdvice(new AccountControllerHandler())
            .build();
    }

    @Test
    public void testCreateUserShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/users")
            .content(JSON)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(accountServiceMock, atLeastOnce()).createUser(any(User.class));
    }

    @Test
    public void testCreateUserWhichAlreadyExistShouldThrowsException() throws Exception {
        doThrow(UserAlreadyExistException.class).when(accountServiceMock).createUser(any());

        mockMvc.perform(post("/api/users")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());

        verify(accountServiceMock, atLeastOnce()).createUser(any(User.class));
    }

    @Test
    public void testGetAuthentationShouldReturnUserInfo() throws Exception {
        SpringSecurityUser userMock = mockSpringUser();
        when(userDetailsServiceMock.loadUserByUsername(any())).thenReturn(userMock);

        mockMvc.perform(get("/api/users")
            .header("Authorization", Mockito.anyString())
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isFound());

        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(any());
        verify(tokenUtilsMock, atLeastOnce()).getUsernameFromToken(any());
    }

    private SpringSecurityUser mockSpringUser() {
        SpringSecurityUser springSecurityUserMock = mock(SpringSecurityUser.class);
        when(springSecurityUserMock.getName()).thenReturn(NAME);
        when(springSecurityUserMock.getEmail()).thenReturn(EMAIL);
        return springSecurityUserMock;
    }
}
