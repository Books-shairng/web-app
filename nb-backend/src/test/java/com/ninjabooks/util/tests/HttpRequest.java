package com.ninjabooks.util.tests;

import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static org.apache.commons.lang.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.util.CollectionUtils.toMultiValueMap;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class HttpRequest
{
    private final String url;
    private final Object[] urlVars;
    private final HttpStatus status;
    private final HttpHeaders headers;
    private final MultiValueMap<String, String> parametrs;
    private final String content;

    private HttpRequest(HttpRequestBuilder builder) {
        this.url = builder.url;
        this.urlVars = builder.urlVars;
        this.status = builder.status;
        this.headers = builder.headers;
        this.parametrs = builder.parametrs;
        this.content = builder.content;
    }

    public String getUrl() {
        return url;
    }

    public Object[] getUrlVars() {
        return urlVars;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public MultiValueMap<String, String> getParametrs() {
        return parametrs;
    }

    public String getContent() {
        return content;
    }

    public static class HttpRequestBuilder
    {
        private String url;
        private Object[] urlVars = new Object[0];
        private HttpStatus status = OK;
        private HttpHeaders headers = new HttpHeaders();
        private MultiValueMap<String, String> parametrs = toMultiValueMap(emptyMap());
        private String content = EMPTY;

        public HttpRequestBuilder(String url) {
            this.url = url;
        }

        public HttpRequestBuilder withUrlVars(Object ... urlVars) {
            this.urlVars = urlVars;
            return this;
        }

        public HttpRequestBuilder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public HttpRequestBuilder withHeaders(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequestBuilder withHeader(String header, String ... values) {
            HttpHeaders headers = new HttpHeaders();
            Stream.of(values).forEach(value -> headers.add(header, value));
            this.headers = headers;
            return this;
        }

        public HttpRequestBuilder withParameters(MultiValueMap<String, String> parametrs) {
            this.parametrs = parametrs;
            return this;
        }

        public HttpRequestBuilder withParameter(String key, String ... values) {
            this.parametrs = toMultiValueMap(ImmutableMap.of(key, asList(values)));
            return this;
        }

        public HttpRequestBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }
}
