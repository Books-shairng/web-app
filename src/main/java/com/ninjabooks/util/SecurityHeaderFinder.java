package com.ninjabooks.util;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SecurityHeaderFinder
{
    private final String BEARER = "Bearer";

    public SecurityHeaderFinder() {
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
