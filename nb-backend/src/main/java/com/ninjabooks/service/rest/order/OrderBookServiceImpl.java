package com.ninjabooks.service.rest.order;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.order.OrderException;
import com.ninjabooks.error.order.OrderMaxLimitException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional(rollbackFor = OrderMaxLimitException.class)
public class OrderBookServiceImpl implements OrderBookService
{
    private static final Logger logger = LogManager.getLogger(OrderBookServiceImpl.class);

    private final static LocalDateTime NOW = LocalDateTime.now();
    private final static int MAXIMUM_LIMIT = 10;

    private final BookDaoService bookService;
    private final UserService userService;
    private final QueueService queueService;

    @Autowired
    public OrderBookServiceImpl(BookDaoService bookService, UserService userService, QueueService queueService) {
        this.bookService = bookService;
        this.userService = userService;
        this.queueService = queueService;
    }

    @Override
    public void orderBook(Long userID, Long bookID) throws OrderException {
        logger.info("User with id: {} wants to order a book", userID);

        createQueue(EntityUtils.getEnity(bookService, bookID), EntityUtils.getEnity(userService, userID));

        logger.info("User with id: {} has successfully ordered a book", userID);
    }

    private void createQueue(Book book, User user) throws OrderException {
        Queue queue = new Queue(NOW);

        if (hasUserOrderedBook(book, user)) {
            throw new OrderException(MessageFormat.format("User: {0} has already ordered this book", user.getId()));
        }

        queue.setUser(user);
        queue.setBook(book);
        queueService.add(queue);

        if (isLimitExceed(user)) {
            throw new OrderMaxLimitException(MessageFormat.format("User: {0} has exceeded the limit", user.getId()));
        }
    }

    /**
     * It perform simple matching, and find if user already ordered book.
     *
     * @param book - book to search
     * @param user - user on which should be perform searching
     * @return matched book
     */

    private boolean hasUserOrderedBook(Book book, User user) {
        return user.getQueues().parallelStream()
            .anyMatch(queue -> queue.getBook().equals(book));
    }

    private boolean isLimitExceed(User user) {
        int size = user.getBorrows().size();
        return size >= MAXIMUM_LIMIT;
    }
}
