package com.ninjabooks.security.controller;

import com.ninjabooks.error.handler.AuthenticationControllerHandler;
import com.ninjabooks.security.service.auth.AuthenticationService;
import com.ninjabooks.security.utils.TokenUtils;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationControllerTest
{
    private static final String HEADER_WITH_TOKEN = "Bearer test_token";
    private static final String TOKEN = "top_secret";
    private final static String JSON_REQUEST =
        "{" +
            "\"email\" : \"user_not_exist@dd.gov\"," +
            "\"password\" : \"pass0\"" +
        "}";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private AuthenticationService authenticationServiceMock;

    @Mock
    private TokenUtils tokenUtilsMock;

    private MockMvc mockMvc;
    private AuthenticationController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new AuthenticationController(authenticationServiceMock, tokenUtilsMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setCustomArgumentResolvers(
                new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()),
                new SitePreferenceHandlerMethodArgumentResolver())
            .setControllerAdvice(new AuthenticationControllerHandler())
            .build();
    }

    @Test
    public void testAutheticationWithCorrectDataShouldResponseOKAndReturnToken() throws Exception {
        when(tokenUtilsMock.generateToken(any(), any())).thenReturn(TOKEN);

        mockMvc.perform(post("/api/auth")
            .content(JSON_REQUEST)
            .contentType(APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(content().string("{\"token\":\"" + TOKEN + "\"}"));

        verify(tokenUtilsMock, atLeastOnce()).generateToken(any(), any());
    }

    @Test
    public void testAutheticationWithIncorrectDataShouldResponseBadRequest() throws Exception {
        when(authenticationServiceMock.authUser(any())).thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(post("/api/auth")
            .content(JSON_REQUEST)
            .contentType(APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        verify(authenticationServiceMock, atLeastOnce()).authUser(any());
    }

    @Test
    public void testRefreshTokenWithWrongDataShouldReturnBadRequest() throws Exception {
        when(authenticationServiceMock.refreshToken(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", HEADER_WITH_TOKEN))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(authenticationServiceMock, atLeastOnce()).refreshToken(any());
    }

    @Test
    public void testRefreshTokenShouldSucceed() throws Exception {
        when(authenticationServiceMock.refreshToken(any())).thenReturn(Optional.of(TOKEN));

        mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", HEADER_WITH_TOKEN))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        verify(authenticationServiceMock, atLeastOnce()).refreshToken(any());
    }

}
