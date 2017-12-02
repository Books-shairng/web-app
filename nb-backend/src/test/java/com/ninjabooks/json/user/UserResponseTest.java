package com.ninjabooks.json.user;

import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.util.constants.DomainTestConstants;

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
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SpringSecurityUser springSecurityUser;

    private UserResponse sut;

    @Before
    public void setUp() throws Exception {
        when(springSecurityUser.getId()).thenReturn(DomainTestConstants.ID);
        when(springSecurityUser.getName()).thenReturn(DomainTestConstants.NAME);
        when(springSecurityUser.getEmail()).thenReturn(DomainTestConstants.EMAIL);

        this.sut = new UserResponse(springSecurityUser);
    }

    @Test
    public void testUserResponseShouldReturnCorrectData() throws Exception {
        assertSoftly(softly -> {
            assertThat(sut.getId()).isEqualTo(DomainTestConstants.ID);
            assertThat(sut.getFirstName()).isEqualTo(DomainTestConstants.FIRSTNAME);
            assertThat(sut.getLastName()).isEqualTo(DomainTestConstants.LASTNAME);
            assertThat(sut.getEmail()).isEqualTo(DomainTestConstants.EMAIL);
        });

    }
}
