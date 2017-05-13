package com.ninjabooks.json.user;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since  1.0
 */
public class UserRequestTest
{
    private static final String EMAIL = "johny.dee@dee.com";
    private static final String PASSWORD = "topSecret123";

    @Test
    public void testgetNameWithCorrectData() throws Exception {
        String firstName = "Johny";
        String lastName = "Dee";
        UserRequest request = new UserRequest(firstName, lastName, EMAIL, PASSWORD);

        Assertions.assertThat(request.getName()).isEqualTo(firstName + " " + lastName);
    }

    @Test
    public void testgetNameWithIncorrectData() throws Exception {
        String firstName = "12312312Jo!!@#65667&(*_)(    h     ny";
        String lastName = "31321@!#D@#!@#4@e@$!@$!@             e";
        UserRequest request = new UserRequest(firstName, lastName, EMAIL, PASSWORD);

        Assertions.assertThat(request.getName()).isEqualTo("Johny Dee");
    }
}
