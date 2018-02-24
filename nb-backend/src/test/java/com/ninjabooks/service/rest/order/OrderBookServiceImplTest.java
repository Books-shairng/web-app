package com.ninjabooks.service.rest.order;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.service.dao.user.UserService;

import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.QUEUE;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;
import static com.ninjabooks.util.constants.DomainTestConstants.USER_FULL;

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
        initMocks(USER, BOOK);

        sut.orderBook(ID, ID);

        verify(queueServiceMock, atLeastOnce()).add(any());
        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWithExceedLimitShouldThrowsException() throws Exception {
        initMocks(userWithOverflowedQueues(), BOOK);

        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(ID, ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testOrderBookWhenUserHasAlreadyOrderedShouldThorwsException() throws Exception {
        initMocks(USER_FULL, BOOK_FULL);

        assertThatExceptionOfType(OrderException.class)
            .isThrownBy(() -> sut.orderBook(ID, ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(bookServiceMock, atLeastOnce()).getById(any());
    }

    private User userWithOverflowedQueues() {
        User user = createFreshEnity();
        List<Queue> borrows = IntStream.range(0, MAX_LIMIT_IN_ORDER)
            .parallel()
            .mapToObj(operand -> QUEUE)
            .collect(Collectors.toList());
        user.setQueues(borrows);

        return user;
    }

    private User createFreshEnity() {
        User user = new User(NAME, EMAIL, PLAIN_PASSWORD);
        user.setId(ID);

        return user;
    }

    private void initMocks(User user, Book book) {
        when(userServiceMock.getById(ID)).thenReturn(Optional.of(user));
        when(bookServiceMock.getById(ID)).thenReturn(Optional.of(book));
    }
}
