package com.ninjabooks.security.endpoint;

import com.ninjabooks.json.error.ErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint
{
    private final ObjectMapper objectMapper;

    @Autowired
    public EntryPointUnauthorizedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String json = errorMessageAsString(request, authException);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(json);
    }

    private String errorMessageAsString(HttpServletRequest request, AuthenticationException e) throws JsonProcessingException {
        return objectMapper.writer()
            .withDefaultPrettyPrinter()
            .writeValueAsString(new ErrorResponse(HttpStatus.UNAUTHORIZED, e, request.getRequestURI()));
    }
}
