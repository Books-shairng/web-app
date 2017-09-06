package com.ninjabooks.json.user;

import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserRequestTest
{
    private static final String EXPECTED_FULLNAME = DomainTestConstants.FIRSTNAME + " " + DomainTestConstants.LASTNAME;

    private UserRequest sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserRequest(DomainTestConstants.FIRSTNAME, DomainTestConstants.LASTNAME,
            DomainTestConstants.EMAIL, DomainTestConstants.PASSWORD);
    }

    @Test
    public void testgetNameWithCorrectData() throws Exception {
        String actual = sut.getName();

        assertThat(actual).isEqualTo(EXPECTED_FULLNAME);
    }

    @Test
    public void testgetNameWithIncorrectData() throws Exception {
        String firstName = "12312312Jo!!@#65667&(*_)(    h     ny";
        String lastName = "31321@!#D@#!@#4@e@$!@$!@             e";
        sut.setFirstName(firstName);
        sut.setLastName(lastName);

        String actual = sut.getName();

        assertThat(actual).isEqualTo(EXPECTED_FULLNAME);
    }
}
