package com.ninjabooks.service.rest.notification;

import com.ninjabooks.domain.Borrow;
import com.ninjabooks.json.notification.BorrowNotification;
import com.ninjabooks.json.notification.QueueNotification;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.service.rest.notification.NotificationService;
import com.ninjabooks.service.rest.notification.NotificationServiceImpl;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class NotificationServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private UserService userServiceMock;

    @Mock
    private ModelMapper modelMapperMock;

    private NotificationService sut;

    @Before
    public void setUp() throws Exception {
        initModelMapperWithProperMocks();
        this.sut = new NotificationServiceImpl(userServiceMock, modelMapperMock);
        DomainTestConstants.USER.setBorrows(Collections.singletonList(initBorrow()));
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(DomainTestConstants.USER));
//        when(borrowNotificationMock.getBorrowDto()).thenReturn(borrowDto);
//        when(borrowNotificationMock.getBookDto()).thenReturn(bookDto);
    }

    @Test
    public void testFindUserBorrowsShouldReturnListWithBorrows() throws Exception {
        List<BorrowNotification> actual = sut.findUserBorrows(DomainTestConstants.ID);
        actual.size();

//        Assertions.assertThat(actual).containsExactly(borrowNotification).usingRecursiveFieldByFieldElementComparator();

//        Borrow borrow = new Borrow();
//        borrow.setBook(BOOK);
//        when(USER.getBorrows()).thenReturn(Collections.singletonList(borrow));
//
//        List<BorrowNotification> actual = sut.findUserBorrows(ID);
//        BorrowNotification borrowNotification = new BorrowNotification(borrow);
//        Assertions.assertThat(actual).containsExactly(borrowNotification);
//
//        verify(userServiceMock, atLeastOnce()).getById(anyLong());
    }

    @Test
    public void testFindUserQueuesShouldRetunListWithQueues() throws Exception {
        List<QueueNotification> actual = sut.findUserQueues(DomainTestConstants.ID);


        verify(userServiceMock, atLeastOnce()).getById(anyLong());
    }

    private Borrow initBorrow() {
        Borrow borrow = new Borrow();
        borrow.setBorrowDate(LocalDate.now());
        borrow.setUser(DomainTestConstants.USER);
        borrow.setBook(DomainTestConstants.BOOK);

        return borrow;
    }

    private void initModelMapperWithProperMocks() {
//        BorrowDto borrowDtoMock = mock(BorrowDto.class);
//        when(borrowDtoMock.getBorrowDate()).thenReturn(BORROW_DATE);
////        borrowDtoMock.setBorrowDate(BORROW_DATE);
//        BookDto bookDtoMock = mock(BookDto.class);
//        when(bookDtoMock.getTitle()).thenReturn(TITLE);
//        when(bookDtoMock.getAuthor()).thenReturn(AUTHOR);
//        when(bookDtoMock.getIsbn()).thenReturn(ISBN);
//        QueueDto queueDtoMock = mock(QueueDto.class);
//        when(queueDtoMock.getOrderDate()).thenReturn(ORDER_DATE);
//
//        when(modelMapperMock.map(any(), any())).thenReturn(bookDtoMock, borrowDtoMock, queueDtoMock);
    }
}
