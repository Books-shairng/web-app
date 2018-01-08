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
    private static final String INCORRECT_FIRST_NAME = "12312312Jo!!@#65667&(*_)(    h     n";
    private static final String INCORRECT_LAST_NAME = "31321@!#D@#!@#4@e@$!@$!@             e";

    private UserRequest sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserRequest(DomainTestConstants.FIRSTNAME, DomainTestConstants.LASTNAME,
            DomainTestConstants.EMAIL, DomainTestConstants.PASSWORD);
    }

    @Test
    public void testGetNameWithCorrectDataShouldReturnExpectedFullName() throws Exception {
        String actual = sut.getName();

        assertThat(actual).isEqualTo(EXPECTED_FULLNAME);
    }

    @Test
    public void testGetNameWithIncorrectFirstNameShouldReturnExpectedFullName() throws Exception {
        sut.setFirstName(INCORRECT_FIRST_NAME);
        String actual = sut.getName();

        assertThat(actual).isEqualTo(EXPECTED_FULLNAME);
    }

    @Test
    public void testGetNameWithIncorrectLastNameShouldReturnExpectedFullName() throws Exception {
        sut.setLastName(INCORRECT_LAST_NAME);
        String actual = sut.getName();

        assertThat(actual).isEqualTo(EXPECTED_FULLNAME);
    }
}
