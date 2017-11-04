package com.ninjabooks.service.rest.comment;

import com.ninjabooks.domain.Book;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.comment.CommentResponseFactory;
import com.ninjabooks.service.dao.book.BookDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService
{
    private final BookDaoService bookDaoService;

    @Autowired
    public CommentServiceImpl(BookDaoService bookDaoService) {
        this.bookDaoService = bookDaoService;
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
}
