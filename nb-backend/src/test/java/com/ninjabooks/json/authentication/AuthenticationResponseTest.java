package com.ninjabooks.json.authentication;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class AuthenticationResponseTest
{
    private static final String TOKEN = "token";

    @Test
    public void testConstructorWithoutArgumentsShouldCreateObjectWithEmptyFields() throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        assertThat(authenticationResponse.getToken()).isNull();
    }

    @Test
    public void testConstructorWithArgumentsShouldCreateExpectedObject() throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(TOKEN);

        assertThat(authenticationResponse.getToken()).isEqualTo(TOKEN);
    }
}
