package com.ninjabooks.controller;

import com.ninjabooks.WebApp;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebApp.class)
@WebAppConfiguration
public class UserControllerTest
{
    private MockMvc mockMvc;

    @Mock
    private UserService userServiceMock;

    private MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @InjectMocks
    private UserController userControllerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userControllerMock).build();
    }

    @Test
    public void testCreateUserShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/users").content(
            "{\"name\":\"Example Example\"," +
            "\"password\":\"topsecret\"," +
            "\"email\":\"newUser@example.com\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(userServiceMock, times(1)).createUser(any(User.class));
    }

    @Test
    public void testGetAuthentationShouldReturnJsonResponse() throws Exception {
        mockHttpServletRequest.addHeader("Authorization", "exmapleToken");
        mockMvc.perform(get("/api/users").requestAttr("Authorization", mockHttpServletRequest)
        .contentType(MediaType.ALL))
        .andReturn();

    }
}
