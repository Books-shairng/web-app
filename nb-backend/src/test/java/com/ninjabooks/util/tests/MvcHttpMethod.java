package com.ninjabooks.util.tests;

import java.util.Map;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface MvcHttpMethod
{
    void doPost(HttpRequest request) throws Exception;
    void doPost(HttpRequest request, Map<String, Object> json) throws Exception;
    void doGet(HttpRequest request) throws Exception;
    void doGet(HttpRequest request, Map<String, Object> json) throws Exception;
}
