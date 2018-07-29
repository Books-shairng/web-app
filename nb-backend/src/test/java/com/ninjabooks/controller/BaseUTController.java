package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest;
import com.ninjabooks.util.tests.MockMvcHttpMethod;
import com.ninjabooks.util.tests.MvcHttpMethod;

import java.util.Map;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.web.servlet.MockMvcBuilder;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public abstract class BaseUTController
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    private MvcHttpMethod httpMethod;

    protected void mockMvc(MockMvcBuilder mvc) throws Exception {
        httpMethod = new MockMvcHttpMethod(mvc.build());
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
}
