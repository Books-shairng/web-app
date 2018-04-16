package com.ninjabooks.security.utils;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
enum Claim
{
    USERNAME("sub"),
    AUDIENCE("audience"),
    CREATED("created");

    private final String key;

    Claim(String key) {
        this.key = key;
    }

    String key() {
        return key;
    }
}
