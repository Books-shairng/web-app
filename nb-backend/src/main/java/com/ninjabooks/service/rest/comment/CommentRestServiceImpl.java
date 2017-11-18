package com.ninjabooks.service.rest.comment;

import com.ninjabooks.domain.*;
import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.comment.CommentResponseFactory;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.comment.CommentDaoService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class CommentRestServiceImpl implements CommentRestService
{
    private final BookDaoService bookDaoService;
    private final CommentDaoService commentDaoService;

    @Autowired
    public CommentRestServiceImpl(BookDaoService bookDaoService, CommentDaoService commentDaoService) {
        this.bookDaoService = bookDaoService;
        this.commentDaoService = commentDaoService;
    }

    @Override
    public Set<CommentResponse> getComments(String isbn) {
        return bookDaoService.getByISBN(isbn)
            .parallel()
            .map(Book::getComments)
            .flatMap(Collection::stream)
            .map(CommentResponseFactory::makeCommentResponse)
            .collect(Collectors.toSet());
    }

    @Override
    public void addComment(String commentText, Long userID, Long bookID) throws CommentException {
        History history = findUserHistory(userID, bookID);
        if (isDateNotOverdue(history) && history.getIsCommented()) {
            Comment comment = new Comment();
            comment.setContent(commentText);
            comment.setBook(getEnityByID(Book.class, bookID));
            comment.setUser(getEnityByID(User.class, userID));
            history.setIsCommented(true);
            commentDaoService.add(comment);
        }
        else {
            throw new CommentException("Unable to add new comment");
        }
    }

    private History findUserHistory(Long userID, Long bookID) {
        Session session = commentDaoService.getSession();
        String hqlQuery = "select h from History h where user_id =:userID and book_id =:bookID";
        Query<History> query = session.createQuery(hqlQuery, History.class);
        query.setParameter("userID", userID);
        query.setParameter("bookID", bookID);

        return query.uniqueResultOptional().orElseThrow(() -> new EntityNotFoundException("History enity not found"));
    }

    private boolean isDateNotOverdue(History history) {
        LocalDate returnDate = history.getReturnDate();
        return returnDate.isBefore(returnDate.plusWeeks(2));
    }

    private <E extends BaseEntity> E getEnityByID(Class<E> clazz, Long id) {
        Session session = commentDaoService.getSession();
        String hqlQuery = "select e from " + clazz.getSimpleName() + " e where id =:param";
        Query<E> query = session.createQuery(hqlQuery, clazz);
        query.setParameter("param", id);
        final String message = MessageFormat.format("Entity with id: {0} not found", id);

        return query.uniqueResultOptional().orElseThrow(() -> new EntityNotFoundException(message));
    }
}
