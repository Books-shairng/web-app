package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Queue;
import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.util.QueueUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
public class RentalHelper
{
    private final BookDaoService bookDaoService;
    private final QueueUtils queueUtils;
    private final QueueService queueService;

    @Autowired
    public RentalHelper(BookDaoService bookDaoService, QueueUtils queueUtils, QueueService queueService) {
        this.bookDaoService = bookDaoService;
        this.queueUtils = queueUtils;
        this.queueService = queueService;
    }

    boolean isBookBorrowed(Book book) {
        return book.getStatus() == BookStatus.BORROWED;
    }

    void updateBook(Book book) {
        bookDaoService.update(book);
    }

    void updateQueue(User user, final Book book) {
        List<Queue> queues = user.getQueues();

        queues.stream()
            .filter(queue -> queue.getBook().equals(book))
            .forEach(queue -> {
                queue.setIsActive(false);
                queueService.update(queue);
            });
    }

    boolean isNotBelongToOtherUserQueue(final Book book, User user) {
        return (isBookContainsInUserQueue(user, book) && hasFirstPosition(user, book))
            || book.getQueues().size() == 0;

    }

    private boolean isBookContainsInUserQueue(final User user, final Book book) {
        List<Queue> queues = user.getQueues();

        return queues.stream()
            .map(Queue::getBook)
            .anyMatch(b -> b.equals(book));
    }

    private boolean hasFirstPosition(final User user, final Book book) {
        List<Queue> queues = user.getQueues();

        Queue queue = queues.stream()
            .filter(q -> q.getBook().equals(book))
            .findFirst()
            .get();

        return queueUtils.computePositionInQueue(queue, user) == 1;
    }
}
