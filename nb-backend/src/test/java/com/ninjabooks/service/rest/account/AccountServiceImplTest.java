package com.ninjabooks.service.rest.account;

import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.service.dao.user.UserService;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AccountServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private UserService userServiceMock;

    private AccountService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new AccountServiceImpl(userServiceMock, passwordEncoderMock);
    }

    @Test
    public void testCreateNewUserWhenAlreadyExistShouldThrowsException() throws Exception {
        when(userServiceMock.getByEmail(EMAIL)).thenReturn(Optional.of(USER));
        assertThatExceptionOfType(UserAlreadyExistException.class)
            .isThrownBy(() -> sut.createUser(USER))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getByEmail(any());
    }

    @Test
    public void testCreateNewUserShouldSucceed() throws Exception {
        doNothing().when(userServiceMock).add(USER);
        sut.createUser(USER);

        verify(userServiceMock, atLeastOnce()).add(any());
    }
}
