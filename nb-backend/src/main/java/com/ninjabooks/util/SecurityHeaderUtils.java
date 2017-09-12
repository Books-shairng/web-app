package com.ninjabooks.util;

import org.springframework.stereotype.Component;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class SecurityHeaderUtils
{
    private final String BEARER = "Bearer";

    public SecurityHeaderUtils() {
    }

    public boolean hasSecurityPattern(String header) {
        if (!header.contains(BEARER))
            throw new IllegalArgumentException("Header contains an unknow type");
        return  true;
    }

    public String extractToken(String header) {
        return header.replaceAll(".*\\b" + BEARER + "\\b.*\\s", "");
    }
}
