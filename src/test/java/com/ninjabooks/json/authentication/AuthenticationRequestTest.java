package com.ninjabooks.json.authentication;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationRequestTest
{
    private String email = "email@example.com";
    private String password = "secret";

    @Test
    public void testCallConstructorWithoutArgumentsShouldCreateObject() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        assertThat(authenticationRequest.getEmail()).isNull();
        assertThat(authenticationRequest.getPassword()).isNull();
    }

    @Test
    public void testCallConstructorWithArgumentsShouldCreateExpectedInstance() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(email, password);

        assertThat(authenticationRequest.getEmail()).isEqualTo(email);
        assertThat(authenticationRequest.getPassword()).isEqualTo(password);
    }
}
