package com.ninjabooks.json.user;

import com.ninjabooks.security.SpringSecurityUser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class UserResponseTest
{
    @Mock
    private SpringSecurityUser springSecurityUser;

    private static final Long ID = 2L;
    private static final String NAME = "John Dee";
    private static final String EMAIL = "johny.dee@dee.com";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(springSecurityUser.getId()).thenReturn(ID);
        when(springSecurityUser.getName()).thenReturn(NAME);
        when(springSecurityUser.getEmail()).thenReturn(EMAIL);
    }

    @Test
    public void testUserResponseShouldReturnCorrectData() throws Exception {
        UserResponse userResponse = new UserResponse(springSecurityUser);
        String[] separatedNames = NAME.split(" ");

        Long actualId = userResponse.getId();
        String actualFirstName = userResponse.getFirstName();
        String actualLastName = userResponse.getLastName();
        String actualEmail = userResponse.getEmail();

        assertSoftly(softly -> {
            assertThat(actualId).isEqualTo(ID);
            assertThat(actualFirstName).isEqualTo(separatedNames[0]);
            assertThat(actualLastName).isEqualTo(separatedNames[1]);
            assertThat(actualEmail).isEqualTo(actualEmail);
        });

    }
}
