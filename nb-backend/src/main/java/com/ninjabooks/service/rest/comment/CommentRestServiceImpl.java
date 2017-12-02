package com.ninjabooks.service.rest.comment;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Comment;
import com.ninjabooks.domain.History;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.comment.CommentResponseFactory;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.comment.CommentDaoService;
import com.ninjabooks.util.entity.EntityUtilsWrapper;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final EntityUtilsWrapper entityUtilsWrapper;

    @Autowired
    public CommentRestServiceImpl(BookDaoService bookDaoService, CommentDaoService commentDaoService, EntityUtilsWrapper entityUtilsWrapper) {
        this.bookDaoService = bookDaoService;
        this.commentDaoService = commentDaoService;
        this.entityUtilsWrapper = entityUtilsWrapper;
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
        if (isDateNotOverdue(history) && !history.getIsCommented()) {
            Comment comment = createCommentEntity(commentText, userID, bookID);
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
        LocalDate today = LocalDate.now();
        long result = returnDate.isAfter(today) ?
            ChronoUnit.DAYS.between(today, returnDate) :
            ChronoUnit.DAYS.between(returnDate, today);

        return !(result >= 14);
    }

    private Comment createCommentEntity(String connent, Long userID, Long bookID) {
        User user = entityUtilsWrapper.getEnity(User.class, userID);
        Book book = entityUtilsWrapper.getEnity(Book.class, bookID);
        return new Comment(connent, user, book);
    }

}
