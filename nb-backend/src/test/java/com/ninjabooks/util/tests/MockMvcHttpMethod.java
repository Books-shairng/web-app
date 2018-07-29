package com.ninjabooks.util.tests;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class MockMvcHttpMethod implements MvcHttpMethod
{
    private final MockMvc mvc;

    public MockMvcHttpMethod(MockMvc mvc) {
        this.mvc = mvc;
    }

    public MockMvc getMvc() {
        return mvc;
    }

    @Override
    public void doPost(HttpRequest request) throws Exception {
        doRequest(request, getBuilder(request, POST));
    }

    @Override
    public void doPost(HttpRequest request, Map<String, Object> json) throws Exception {
        MvcResult result = doRequest(request, getBuilder(request, POST));
        matchJson(json, result);
    }

    @Override
    public void doGet(HttpRequest request) throws Exception {
        doRequest(request, getBuilder(request, GET));
    }

    @Override
    public void doGet(HttpRequest request, Map<String, Object> json) throws Exception {
        MvcResult result = doRequest(request, getBuilder(request, GET));
        matchJson(json, result);
    }

    private MockHttpServletRequestBuilder getBuilder(HttpRequest request, org.springframework.http.HttpMethod method) {
        return  method == GET ?
            get(request.getUrl(), request.getUrlVars()) :
            post(request.getUrl(), request.getUrlVars());
    }

    private MvcResult doRequest(HttpRequest request, MockHttpServletRequestBuilder builder) throws Exception {
        return mvc.perform(builder
            .headers(request.getHeaders())
            .params(request.getParametrs())
            .content(request.getContent())
            .contentType(APPLICATION_JSON_UTF8_VALUE))
            .andDo(print())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().is(request.getStatus().value()))
            .andReturn();
    }

    private void matchJson(Map<String, Object> json, MvcResult result) throws UnsupportedEncodingException {
        String content = result.getResponse().getContentAsString();
        json.forEach((key, value) -> {
            Object read = JsonPath.read(content, key);
            assertThat(read).isEqualTo(value);
        });
    }
}
