package com.ninjabooks.json.authentication;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationRequestTest
{
    private static final String EMAIL = "email@example.com";
    private static final String PASSWORD = "secret";

    @Test
    public void testCallConstructorWithoutArgumentsShouldCreateObject() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        assertThat(authenticationRequest.getEmail()).isNull();
        assertThat(authenticationRequest.getPassword()).isNull();
    }

    @Test
    public void testCallConstructorWithArgumentsShouldCreateExpectedInstance() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(EMAIL, PASSWORD);

        assertThat(authenticationRequest.getEmail()).isEqualTo(EMAIL);
        assertThat(authenticationRequest.getPassword()).isEqualTo(PASSWORD);
    }
}
