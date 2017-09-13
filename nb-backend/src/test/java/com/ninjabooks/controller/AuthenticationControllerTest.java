package com.ninjabooks.controller;

import com.ninjabooks.error.handler.AuthenticationControllerHandler;
import com.ninjabooks.security.SpringSecurityUser;
import com.ninjabooks.security.TokenUtils;
import com.ninjabooks.util.SecurityHeaderUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    private final static  String JSON_REQUEST =
    "{" +
        "\"email\" : \"user_not_exist@dd.gov\"," +
        "\"password\" : \"pass0\"" +
    "}";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private TokenUtils tokenUtilsMock;

    @Mock
    private UserDetailsService userDetailsServiceMock;

    @Mock
    private SecurityHeaderUtils securityHeaderFinderMock;

    private MockMvc mockMvc;
    private AuthenticationController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new AuthenticationController(authenticationManagerMock, tokenUtilsMock,
            userDetailsServiceMock, securityHeaderFinderMock);

        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setCustomArgumentResolvers(
                new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()),
                new SitePreferenceHandlerMethodArgumentResolver())
            .setControllerAdvice(new AuthenticationControllerHandler())
            .build();
    }

    @Test
    public void testAutheticationWithCorrectDataShouldResponseOKAndReturnToken() throws Exception {
        String token = "top_secret";
        when(tokenUtilsMock.generateToken(any(), any())).thenReturn(token);

        mockMvc.perform(post("/api/auth")
                .content(JSON_REQUEST)
                .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(  "{\"token\":\""+ token +"\"}"));

        verify(tokenUtilsMock, atLeastOnce()).generateToken(any(), any());
    }

    @Test
    public void testAutheticationWithIncorrectDataShouldResponseBadRequest() throws Exception {
        when(userDetailsServiceMock.loadUserByUsername(any())).thenThrow(UsernameNotFoundException.class);

        mockMvc.perform(post("/api/auth")
                .content(JSON_REQUEST)
                .contentType(APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8));

        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(anyString());
    }

    @Test
    public void testRefreshTokenWithWrongDataShouldReturnBadRequest() throws Exception {
        SpringSecurityUser user = initSpringUser();
        when(userDetailsServiceMock.loadUserByUsername(any())).thenReturn(user);

        mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", HEADER_WITH_TOKEN))
            .andExpect(status().isBadRequest())
            .andDo(print());

        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(any());
    }

    @Test
    public void testRefreshTokenShouldSucceed() throws Exception {
        SpringSecurityUser user = initSpringUser();
        when(userDetailsServiceMock.loadUserByUsername(any())).thenReturn(user);
        when(tokenUtilsMock.canTokenBeRefreshed(any(), any())).thenReturn(true);
        when(tokenUtilsMock.refreshToken(any())).thenReturn(any());

        mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", HEADER_WITH_TOKEN))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8));

       verify(tokenUtilsMock, atLeastOnce()).refreshToken(any());
    }

    private SpringSecurityUser initSpringUser() {
        SpringSecurityUser user = new SpringSecurityUser();
        user.setLastPasswordReset(LocalDateTime.now());
        return user;
    }
}
