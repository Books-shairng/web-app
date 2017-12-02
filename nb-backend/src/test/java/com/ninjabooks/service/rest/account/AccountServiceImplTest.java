package com.ninjabooks.service.rest.account;

import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.constants.DomainTestConstants;

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
    public MockitoRule mockitoRule = MockitoJUnit.rule();

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
        when(userServiceMock.getByEmail(DomainTestConstants.EMAIL)).thenReturn(Optional.of(DomainTestConstants.USER));
        assertThatExceptionOfType(UserAlreadyExistException.class)
            .isThrownBy(() -> sut.createUser(DomainTestConstants.USER))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getByEmail(any());
    }

    @Test
    public void testCreateNewUserShouldSucceed() throws Exception {
        doNothing().when(userServiceMock).add(DomainTestConstants.USER);
        sut.createUser(DomainTestConstants.USER);

        verify(userServiceMock, atLeastOnce()).add(any());
    }
}
