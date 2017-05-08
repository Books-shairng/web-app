package com.ninjabooks.controller;

import com.ninjabooks.configuration.AppConfig;
import com.ninjabooks.security.AuthenticationTokenFilter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
            .build();

    }

    @Test
    @Sql(value = "/queries/import.sql")
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
        MvcResult result = mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    //todo fix this test
    @Test
    @Ignore("/api/refresh not implemented in routing table")
    public void testAuthenticationRequestShouldSucceed() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/refresh")
            .header("Authoriazation", Mockito.anyString()))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().json("{'token' : 'secretToken'}"))
            .andReturn();

        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
