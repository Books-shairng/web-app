package com.ninjabooks.service.rest.borrow;

import com.ninjabooks.domain.*;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.util.QueueUtils;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

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

    public boolean isBookBorrowed(Book book) {
        return book.getStatus() == BookStatus.BORROWED;
    }

    public void updateBook(Book book) {
        bookDaoService.update(book);
    }

    public void updateQueue(User user, final Book book) {
        List<Queue> queues = user.getQueues();

        queues.stream()
            .filter(queue -> queue.getBook().equals(book))
            .forEach(queue -> {
                queue.setIsActive(false);
                queueService.update(queue);
            });
    }

    public boolean isNotBelongToOtherUserQueue(final Book book, User user) {
        return (isBookContainsInUserQueue(user, book) && hasFirstPosition(user, book))
            || book.getQueues().size() == 0;

    }

    public Book findBookByQRCode(String qrCodeData) {
        Session currentSession = bookDaoService.getSession();
        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = builder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        Join<Book, QRCode> qrCodeJoin = root.join("QRCode");

        criteriaQuery
            .select(root)
            .where(builder.equal(qrCodeJoin.get("data"), qrCodeData));

        Optional<Book> book = currentSession.createQuery(criteriaQuery).uniqueResultOptional();
        String message = MessageFormat.format("Book not found by given qr code: {0}", qrCodeData);

        return book.orElseThrow(() -> new EntityNotFoundException(message));
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
