package com.ninjabooks.service.rest;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.order.OrderException;
import com.ninjabooks.service.rest.book.BookRestService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.service.rest.order.OrderBookService;
import com.ninjabooks.service.rest.order.OrderBookServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderBookServiceImplTest
{
    private static final String AUTHOR = "C. Ho, R. Harrop, C. Schaefer";
    private static final String TITLE = "Pro Spring, 4th Edition";
    private static final String ISBN = "978-1430261513";

    private Book book = new Book(TITLE, AUTHOR, ISBN);
    private Long userID = 1L;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    private BookRestService bookServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private QueueService queueServiceMock;

    private OrderBookService sut;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sut = new OrderBookServiceImpl(bookServiceMock, userServiceMock, queueServiceMock);
    }

    @Test
    public void testOrderBookShouldAddBookToTheUserQueue() throws Exception {
        //given
        User user = new User();
        when(userServiceMock.getById(userID)).thenReturn(Optional.of(user));

        Queue queue = new Queue(LocalDateTime.now());
        queueServiceMock.add(queue);
        when(queueServiceMock.getById(1L)).thenReturn(Optional.of(queue));

        //when
        sut.orderBook(userID, book.getTitle());

        //then
        verify(queueServiceMock, atLeastOnce()).add(queue);
    }

    @Test(expected = OrderException.class)
    public void testOrderBookWithExceedLimitShouldThrowsException() throws Exception {
        User userMock = Mockito.mock(User.class);

        //given
        when(userServiceMock.getById(userID)).thenReturn(Optional.of(userMock));
        when(userMock.getBorrows()).thenReturn(generateBorrowList(11));

        //whem
        sut.orderBook(userID, book.getTitle());

        //then
        verify(userServiceMock, atLeastOnce()).getById(userID);
    }

    @Test
    public void testOrderBookWithMultipleBooksShouldChooseShortestQueue() throws Exception {

    }

    private List<Borrow> generateBorrowList(int size) {
        List<Borrow> borrows = new ArrayList<>(size);
        for (int i = 0; i < size; i++)
            borrows.add(new Borrow());

        return borrows;
    }
}
