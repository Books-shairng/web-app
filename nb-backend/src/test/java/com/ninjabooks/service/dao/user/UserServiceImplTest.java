package com.ninjabooks.service.dao.user;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserServiceImplTest
{
    private static final Optional<User> EXPECTED_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.USER);
    private static final Optional<User> EMPTY_OPTIONAL = Optional.empty();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private UserDao userDaoMock;

    @Mock
    private GenericDao<User, Long> genericDaoMock;

    private UserService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserServiceImpl(genericDaoMock, userDaoMock);
    }

    @Test
    public void testGetByNameShouldReturnExepectedOptional() throws Exception {
        when(userDaoMock.getByName(DomainTestConstants.NAME)).thenReturn(EXPECTED_OPTIONAL);
        Optional<User> actual = sut.getByName(DomainTestConstants.NAME);

        assertThat(actual).contains(DomainTestConstants.USER);
        verify(userDaoMock, atLeastOnce()).getByName(any());
    }

    @Test
    public void testGetByEmailShouldReturnExpectedOptional() throws Exception {
        when(userDaoMock.getByEmail(DomainTestConstants.EMAIL)).thenReturn(EXPECTED_OPTIONAL);
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        assertThat(actual).contains(DomainTestConstants.USER);
        verify(userDaoMock, atLeastOnce()).getByEmail(any());
    }

    @Test
    public void testGetByEmailWhichNotExistShouldReturnEmptyOptional() throws Exception {
        when(userDaoMock.getByEmail(DomainTestConstants.EMAIL)).thenReturn(EMPTY_OPTIONAL);
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        assertThat(actual).isEmpty();
        verify(userDaoMock, atLeastOnce()).getByEmail(any());
    }

    @Test
    public void testGetByNameWhichNotExistShouldReturnEmptyOptional() throws Exception {
        when(userDaoMock.getByName(DomainTestConstants.EMAIL)).thenReturn(EMPTY_OPTIONAL);
        Optional<User> actual = sut.getByName(DomainTestConstants.EMAIL);

        assertThat(actual).isEmpty();
        verify(userDaoMock, atLeastOnce()).getByName(any());
    }
}
