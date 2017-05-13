package com.ninjabooks.json;

import com.ninjabooks.json.authentication.AuthenticationResponse;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class AuthenticationResponseTest
{
    private String token = "token";

    @Test
    public void testConstructorWithoutArgumentsShouldCreateObjectWithEmptyFields() throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        assertThat(authenticationResponse.getToken()).isNull();
    }

    @Test
    public void testConstructorWithArgumentsShouldCreateExpectedObject() throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);

        assertThat(authenticationResponse.getToken()).isEqualTo(token);
    }
}
