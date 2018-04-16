package com.ninjabooks.security.utils;

import java.util.Optional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class SecurityHeaderUtils
{
    private static final String BEARER = "Bearer";

    private SecurityHeaderUtils() {
    }

    public static String extractTokenFromHeader(String header) {
        return obtainTokenFromRequest(header)
            .filter(s -> !s.isEmpty())
            .orElseThrow(() -> new IllegalArgumentException("Header does not contains proper token"));
    }

    private static Optional<String> obtainTokenFromRequest(String request) {
        return hasSecurityPattern(request) ? Optional.ofNullable(extract(request)) : Optional.empty();
    }

    private static boolean hasSecurityPattern(String header) {
        return header.contains(BEARER);
    }

    private static String extract(String header) {
        return header.replaceAll(".*\\b" + BEARER + "\\b.*\\s+|\\s+|.*\\b" + BEARER + "\\b.*", "");
    }
}
