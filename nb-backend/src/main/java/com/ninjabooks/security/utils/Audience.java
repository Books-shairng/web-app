package com.ninjabooks.security.utils;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
enum Audience
{
    UNKNOWN, WEB, MOBILE, TABLET;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
