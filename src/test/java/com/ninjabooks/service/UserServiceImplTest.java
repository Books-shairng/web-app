package com.ninjabooks.service;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.UserAlreadyExistException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class UserServiceImplTest
{
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private UserDao userDaoMock;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private UserService userServiceMock;

    private static final String NAME = "John Dee";
    private static final String EMAIL = "john.dee@exmaple.com";
    private static final String PASSWORD = "Johny!Dee123";


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.userServiceMock = new UserServiceImpl(userDaoMock, passwordEncoderMock);
    }

    @Test
    public void testCreateNewUserWhenAlreadyExistShouldThrowsException() throws Exception {
        User user = new User(NAME, EMAIL, PASSWORD);

        //given
        when(userDaoMock.getByEmail(user.getEmail())).thenReturn(user);

        //when
        exception.expect(UserAlreadyExistException.class);

        //then
        userServiceMock.createUser(user);
    }

    @Test
    public void testCreateNewUserShouldSucceed() throws Exception {
        User user = new User(NAME, EMAIL, PASSWORD);

        userServiceMock.createUser(user);
        verify(userDaoMock).add(Mockito.any(User.class));
    }
}
