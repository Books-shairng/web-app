package com.ninjabooks.service;

import com.ninjabooks.WebApp;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.UserAlreadyExistException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
@ContextConfiguration(classes = WebApp.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceImplTest
{
    @Mock
    private UserDao userDaoMock;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userServiceMock;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.userServiceMock = new UserServiceImpl(userDaoMock, passwordEncoder);
    }

    @Test
    public void testCreateNewUserWhenAlreadyExistShouldThrowsException() throws Exception {
        User user = new User("Name", "exmaple@exmaple.com", "secret");

        //given
        when(userDaoMock.getByEmail(user.getEmail())).thenReturn(user);

        //when
        exception.expect(UserAlreadyExistException.class);

        //then
        userServiceMock.createUser(user);
    }

    @Test
    public void testCreeteNewUserShouldSucceed() throws Exception {
        User user = new User("Name", "exmaple@exmaple.com", "secret");


        User peristedUser = userServiceMock.createUser(user);

        assertThat(peristedUser).isNotNull();
    }
}
