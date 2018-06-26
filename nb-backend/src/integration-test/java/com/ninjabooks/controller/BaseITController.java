package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.util.tests.HttpRequest;
import com.ninjabooks.util.tests.MockMvcHttpMethod;
import com.ninjabooks.util.tests.MvcHttpMethod;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class BaseITController extends AbstractBaseIT
{
    @Autowired
    private WebApplicationContext wac;

    MvcHttpMethod httpMethod;

    @Before
    public void setUp() throws Exception {
        httpMethod = new MockMvcHttpMethod(webAppContextSetup(wac).build());
    }

    public void doPost(HttpRequest request) throws Exception {
        httpMethod.doPost(request);
    }

    public void doPost(HttpRequest request, Map<String, Object> json) throws Exception {
        httpMethod.doPost(request, json);
    }

    public void doGet(HttpRequest request) throws Exception {
        httpMethod.doGet(request);
    }

    public void doGet(HttpRequest request, Map<String, Object> json) throws Exception {
        httpMethod.doGet(request, json);
    }

    public static LinkedMultiValueMap<String, String> convertParams(Map<String, List<String>> params) {
        return new LinkedMultiValueMap<>(params);
    }
}
