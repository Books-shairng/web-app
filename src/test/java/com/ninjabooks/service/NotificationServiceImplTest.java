package com.ninjabooks.service;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class NotificationServiceImplTest
{
    @Mock
    private UserDao userDaoMock;

    private NotificationService notificationServiceMock;

    private static final String NAME = "John Dee";
    private static final String EMAIL = "john.dee@exmaple.com";
    private static final String PASSWORD = "Johny!Dee123";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.notificationServiceMock = new NotificationServiceImpl(userDaoMock);
    }

    @Test
    public void testFindUserBorrowsShouldReturnListWithBorrows() throws Exception {
        User user = new User(NAME, EMAIL, PASSWORD);
        //when
        when(userDaoMock.getById(anyLong())).thenReturn(user);
        //then
        notificationServiceMock.findUserQueues(anyLong());
        //verify
        verify(userDaoMock, times(1)).getById(anyLong());
    }

    @Test
    public void testFindUserQueuesShouldRetunListWithQueues() throws Exception {
        User user = new User(NAME, EMAIL, PASSWORD);

        when(userDaoMock.getById(anyLong())).thenReturn(user);
        notificationServiceMock.findUserQueues(anyLong());

        verify(userDaoMock, atLeastOnce()).getById(anyLong());
    }
}
