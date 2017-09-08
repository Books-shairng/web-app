package com.ninjabooks.service.rest.order;

import com.ninjabooks.error.order.OrderMaxLimitException;
import com.ninjabooks.service.dao.book.BookService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Ignore(value = "Write new test implementation for this class")
public class OrderBookServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookService bookServiceMock;

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
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.ofNullable(DomainTestConstants.USER));
        when(queueServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.ofNullable(DomainTestConstants.QUEUE));

        sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID);

        verify(queueServiceMock, atLeastOnce()).add(any());
    }

    @Test
    public void testOrderBookWithExceedLimitShouldThrowsException() throws Exception {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.ofNullable(DomainTestConstants.USER));
        when(DomainTestConstants.USER.getBorrows().size()).thenReturn(20);

        Assertions.assertThatExceptionOfType(OrderMaxLimitException.class).
            isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWithMultipleBooksShouldChooseShortestQueue() throws Exception {

    }

}
