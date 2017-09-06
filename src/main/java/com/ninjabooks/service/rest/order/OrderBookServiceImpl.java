package com.ninjabooks.service.rest.order;

import com.ninjabooks.domain.Book;
import com.ninjabooks.error.order.OrderMaxLimitException;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.order.OrderException;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.service.rest.book.BookRestService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class OrderBookServiceImpl implements OrderBookService
{
    private static final Logger logger = LogManager.getLogger(OrderBookServiceImpl.class);

    private final static LocalDateTime NOW = LocalDateTime.now();
    private final static int MAXIMUM_LIMIT = 10;

    private final BookRestService bookService;
    private final UserService userService;
    private final QueueService queueService;

    @Autowired
    public OrderBookServiceImpl(BookRestService bookService, UserService userService, QueueService queueService) {
        this.bookService = bookService;
        this.userService = userService;
        this.queueService = queueService;
    }

    @Override
    public void orderBook(Long userID, String title) throws OrderException {
        logger.info("User with id:{" + userID +"} wants to order a book with title:{" + title + "}");

        User user = userService.getById(userID).get();
        if (isLimitExceed(user))
            errorInfromation(userID);

        createQueue(new Book(), user);

        logger.info("User with id:{" + userID +"} has successfully ordered a book with title:{" + title +"}");
    }

    private void createQueue(Book book, User user) {
        Queue queue = new Queue(NOW);
        queue.setUser(user);
        queue.setBook(book);
        queueService.add(queue);
    }

    private void errorInfromation(Long userID) throws OrderException {
        String message = "User:{" + userID + "} has exceeded the limit";
        logger.error(message);
        throw new OrderMaxLimitException(message);
    }

    private boolean isLimitExceed(User user) {
        return user.getBorrows().size() > MAXIMUM_LIMIT;
    }
}
