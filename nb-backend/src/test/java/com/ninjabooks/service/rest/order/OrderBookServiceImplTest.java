package com.ninjabooks.service.rest.order;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderBookServiceImplTest
{
    private static final int MAX_LIMIT_IN_ORDER = 10;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

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
        initMocks(DomainTestConstants.USER, DomainTestConstants.BOOK);

        sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID);

        verify(queueServiceMock, atLeastOnce()).add(any());
        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWithExceedLimitShouldThrowsException() throws Exception {
        initMocks(userWithOverflowedQueues(), DomainTestConstants.BOOK);

        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWhenUserHasAlreadyOrderedShouldThorwsException() throws Exception {
        initMocks(DomainTestConstants.USER_FULL, DomainTestConstants.BOOK_FULL);

        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    private User userWithOverflowedQueues() {
        User user = createFreshEnity();
        List<Queue> borrows = IntStream.range(0, MAX_LIMIT_IN_ORDER)
            .parallel()
            .mapToObj(operand -> DomainTestConstants.QUEUE)
            .collect(Collectors.toList());
        user.setQueues(borrows);

        return user;
    }

    private User createFreshEnity() {
        User user = new User(DomainTestConstants.NAME, DomainTestConstants.EMAIL, DomainTestConstants.PLAIN_PASSWORD);
        user.setId(DomainTestConstants.ID);

        return user;
    }

    private void initMocks(User user, Book book) {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(user));
        when(bookServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(book));
    }
}
