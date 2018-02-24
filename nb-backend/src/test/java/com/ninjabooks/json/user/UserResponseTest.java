package com.ninjabooks.json.user;

import com.ninjabooks.security.user.SpringSecurityUser;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.FIRSTNAME;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.LASTNAME;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserResponseTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private SpringSecurityUser springSecurityUser;

    private UserResponse sut;

    @Before
    public void setUp() throws Exception {
        when(springSecurityUser.getId()).thenReturn(ID);
        when(springSecurityUser.getName()).thenReturn(NAME);
        when(springSecurityUser.getEmail()).thenReturn(EMAIL);

        this.sut = new UserResponse(springSecurityUser);
    }

    @Test
    public void testUserResponseShouldReturnCorrectData() throws Exception {
        assertSoftly(softly -> {
            assertThat(sut.getId()).isEqualTo(ID);
            assertThat(sut.getFirstName()).isEqualTo(FIRSTNAME);
            assertThat(sut.getLastName()).isEqualTo(LASTNAME);
            assertThat(sut.getEmail()).isEqualTo(EMAIL);
        });

    }
}
