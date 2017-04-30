package com.ninjabooks.controller;

import com.ninjabooks.WebApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
@ContextConfiguration(classes = WebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AuthenticationControllerTest
{
    @InjectMocks
    private AuthenticationController authenticationController;


    @Mock
    private FilterChainProxy filterChainProxy;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
        DeviceResolverRequestFilter deviceResolverRequestFilter = new DeviceResolverRequestFilter();

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
            .addFilters(filterChainProxy, deviceResolverRequestFilter).build();
    }

    @Test
    public void loginWithCorrectData() throws Exception {
        mockMvc.perform(post("/api/auth")
            .content(
                "{\"password\":\"topsecret\"," +
                "\"email\":\"newUser@example.com\"}")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
