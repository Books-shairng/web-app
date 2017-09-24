package com.ninjabooks.service.rest.order;

import com.ninjabooks.domain.User;
import com.ninjabooks.error.order.OrderException;
import com.ninjabooks.error.order.OrderMaxLimitException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderBookServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookDaoService bookServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private QueueService queueServiceMock;

    private OrderBookService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new OrderBookServiceImpl(bookServiceMock, userServiceMock, queueServiceMock);
    }

    @Test
    public void testOrderBookShouldAddBookToTheUserQueue() throws Exception {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(DomainTestConstants.USER));
        when(bookServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(DomainTestConstants.BOOK));

        sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID);

        verify(queueServiceMock, atLeastOnce()).add(any());
        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWithExceedLimitShouldThrowsException() throws Exception {
        User user = DomainTestConstants.USER;
        when(user.getBorrows().size()).thenReturn(11);

        assertThatExceptionOfType(OrderMaxLimitException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWhenUserHasAlreadyOrderedShouldThorwsException() throws Exception {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(DomainTestConstants.USER_FULL));
        when(bookServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(DomainTestConstants.BOOK_FULL));

        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

}
