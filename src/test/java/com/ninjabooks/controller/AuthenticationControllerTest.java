package com.ninjabooks.controller;

import com.ninjabooks.configuration.AppConfig;
import com.ninjabooks.error.handler.AuthenticationControllerHandler;
import com.ninjabooks.security.AuthenticationTokenFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.DeviceResolverRequestFilter;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.mobile.device.site.SitePreferenceHandlerMethodArgumentResolver;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
@ContextConfiguration(classes = AppConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class AuthenticationControllerTest
{
    @Autowired
    private AuthenticationController authenticationController;

    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        this.mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
            .addFilters(authenticationTokenFilter, new DeviceResolverRequestFilter())
            .setCustomArgumentResolvers(
                new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()),
                new SitePreferenceHandlerMethodArgumentResolver())
            .setControllerAdvice(new AuthenticationControllerHandler())
            .build();

    }

    @Test
    public void testAutheticationWithCorrectDataShouldRsponseOk() throws Exception {
        String body = "{\"email\" : \"email0@sb.gov\",\"password\" : \"pass0\"}";
        mockMvc.perform(post("/api/auth")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andDo(log())
                .andExpect(status().isOk());
    }

    @Test
    public void testAutheticationWithIncorrectDataShouldResponseBadRequest() throws Exception {
        String body = "{\"email\" : \"user_not_exist@dd.gov\",\"password\" : \"pass0\"}";
        MvcResult result = mockMvc.perform(post("/api/auth")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testRefreshTokenShouldResturnStatus401() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG9sZi5oaXRsZXJAcmVpY2guZGUiLCJhdWRpZW5jZSI6IndlYiIsIm" +
            "NyZWF0ZWQiOjE0OTU5NzMyNjk1OTYsImV4cCI6MTQ5NjU3ODA2OX0.hQgUsm-KqZOfkT7iczgK70oOEapuQOW5Qvh1o0HziPk_anOQ" +
            "B0D9huy-b86Uw2IbwZMLzgFl-PjKOnv5l5nFyg";

        MvcResult mvcResult = mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", "Bearer " +  token))
            .andExpect(status().isUnauthorized())
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @Sql(value = "classpath:/queries/import.sql")
    public void testRefreshTokenShouldSucceed() throws Exception {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlbWFpbDBAc2IuZ292IiwiYXVkaWVuY2UiOiJ3ZWIiLCJjcmVhdGVkIjp7Im1" +
            "vbnRoIjoiTUFZIiwieWVhciI6MjAxNywiZGF5T2ZNb250aCI6MjgsImhvdXIiOjE1LCJtaW51dGUiOjQ4LCJtb250aFZhbHVlIjo1LCJuY" +
            "W5vIjo5MjEwMDAwMDAsInNlY29uZCI6MjUsImRheU9mV2VlayI6IlNVTkRBWSIsImRheU9mWWVhciI6MTQ4LCJjaHJvbm9sb2d5Ijp7Iml" +
            "kIjoiSVNPIiwiY2FsZW5kYXJUeXBlIjoiaXNvODYwMSJ9fSwiZXhwIjoxNDk2NTg0MTA1fQ.koMli4OWcJpiD3NglJzIjLHp3G7xB3msHf" +
            "qVG6dq3IhwwkIgcyxTvAweknv5Ij-APApN-aMOv_l-s0wwU2dKHw";

        MvcResult mvcResult = mockMvc.perform(get("/api/auth/refresh")
            .header("Authorization", "Bearer " +  token))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
