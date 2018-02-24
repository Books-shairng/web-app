package com.ninjabooks.security.service;

import com.ninjabooks.domain.User;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.CommonUtils;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserAuthServiceTest
{
    private static final Optional<User> USER_OPTIONAL = CommonUtils.asOptional(USER);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private UserService userServiceMock;

    private UserAuthService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserAuthService(userServiceMock);
    }

    @Test
    public void testLoadUserByUsernameShouldReturnExpectedFields() throws Exception {
        when(userServiceMock.getByEmail(EMAIL)).thenReturn(USER_OPTIONAL);

        SpringSecurityUser actual = (SpringSecurityUser) sut.loadUserByUsername(EMAIL);
        assertThat(actual).extracting("id", "name", "email")
            .containsExactly(ID, NAME, EMAIL);

        verify(userServiceMock, atLeastOnce()).getByEmail(any());
    }

    @Test
    public void testLoadUserByUsernameShouldThrowsExceptionWhenEmpty() throws Exception {
        when(userServiceMock.getByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> sut.loadUserByUsername(EMAIL))
            .withMessageContaining("User not found")
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getByEmail(any());
    }
}
