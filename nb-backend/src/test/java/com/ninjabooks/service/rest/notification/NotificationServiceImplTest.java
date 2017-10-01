package com.ninjabooks.service.rest.notification;

import com.ninjabooks.domain.User;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.BorrowDto;
import com.ninjabooks.dto.QueueDto;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class NotificationServiceImplTest
{
    private static final Optional<User> FULL_USER_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.USER_FULL);
    private static final Optional<User> USER_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.USER);
    private static final int EXPECTED_SIZE = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserService userServiceMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private BookDto bookDtoMock;

    @Mock
    private BorrowDto borrowDtoMock;

    @Mock
    private QueueDto queueDtoMock;

    private NotificationService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new NotificationServiceImpl(userServiceMock, modelMapperMock);
        when(userServiceMock.getById(any())).thenReturn(FULL_USER_OPTIONAL);
    }

    @Test
    public void testFindUserBorrowsShouldReturnListOfBorrows() throws Exception {
        prepareOngoingStubs(bookDtoMock, borrowDtoMock);
        List<BorrowNotification> actual = sut.findUserBorrows(DomainTestConstants.ID);

        assertThat(actual).usingFieldByFieldElementComparator()
            .extracting("bookDto", "borrowDto")
            .containsExactly(tuple(bookDtoMock, borrowDtoMock));

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(modelMapperMock, atLeastOnce()).map(any(), any());
    }

    @Test
    public void testFindUserBorrowsWhenUserNotHaveBorrowsShouldReturnEmptyList() throws Exception {
        when(userServiceMock.getById(any())).thenReturn(USER_OPTIONAL);
        List<BorrowNotification> actual = sut.findUserBorrows(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testFindUserBorrowsShouldReturnExpectedSizeOfList() throws Exception {
        prepareOngoingStubs(bookDtoMock, borrowDtoMock);
        List<BorrowNotification> actual = sut.findUserBorrows(DomainTestConstants.ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testFindUserQueuesShouldReturnListOfQueues() throws Exception {
        NativeQuery nativeQueryMock = mock(NativeQuery.class);
        Session sessionMock = mock(Session.class);
        when(userServiceMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.createNativeQuery(anyString())).thenReturn(nativeQueryMock);
        prepareOngoingStubs(bookDtoMock, queueDtoMock);
        List<QueueNotification> actual = sut.findUserQueues(DomainTestConstants.ID);

        assertThat(actual).usingFieldByFieldElementComparator()
            .extracting("bookDto", "queueDto")
            .containsExactly(tuple(bookDtoMock, queueDtoMock));

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(modelMapperMock, atLeastOnce()).map(any(), any());
    }

    @Test
    public void testFindUserQueuesWhenUserNotHaveQueuesShouldReturnEmptyList() throws Exception {
        when(userServiceMock.getById(any())).thenReturn(USER_OPTIONAL);
        List<QueueNotification> actual = sut.findUserQueues(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testFindUserQueuesShouldReturnExpectedSizeOfList() throws Exception {
        NativeQuery nativeQueryMock = mock(NativeQuery.class);
        Session sessionMock = mock(Session.class);
        when(userServiceMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.createNativeQuery(anyString())).thenReturn(nativeQueryMock);
        prepareOngoingStubs(bookDtoMock, queueDtoMock);
        List<QueueNotification> actual = sut.findUserQueues(DomainTestConstants.ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    private <E, T> void prepareOngoingStubs(E e, T t) {
        when(modelMapperMock.map(any(), any())).thenReturn(e, t);
    }

}
