package com.ninjabooks.controller;

import com.ninjabooks.domain.User;
import com.ninjabooks.security.SpringSecurityUser;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.service.UserService;
import com.ninjabooks.util.SecurityHeaderFinder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class UserControllerTest
{
    @Mock
    private UserService userServiceMock;

    @Mock
    private TokenUtils tokenUtilsMock;
    @Mock
    private UserDetailsService userDetailsServiceMock;

    @Mock
    private SpringSecurityUser springSecurityUser;

    @Mock
    private SecurityHeaderFinder securityHeaderFinder;

    @InjectMocks
    private UserController userControllerMock;

    private MockMvc mockMvc;

    private static final Long ID = 1L;
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME= "Dee";
    private static final String EMAIL = "john.dee@exmaple.com";
    private static final String PASSWORD = "Johny!Dee123";

    private final String json =
        "{" +
            "\"firstName\":\""+ FIRST_NAME + "\"," +
            "\"lastName\":\""+ LAST_NAME + "\"," +
            "\"email\":\""+ EMAIL+ "\"," +
            "\"password\":\""+ PASSWORD + "\"}" +
        "}";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userControllerMock).build();
    }

    @Test
    public void testCreateUserShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/users")
            .content(json)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(userServiceMock, times(1)).createUser(any(User.class));
    }

    //todo Find any solution to deal with this problem
    @Test
    @Ignore("Powermockito not working with 2.0+ mockito, in this case code is untestable")
    public void testGetAuthentationShouldReturnJsonResponse() throws Exception {
        mockMvc.perform(get("/api/users")
        .header("Authorization", Mockito.anyString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isFound());

    }
}
