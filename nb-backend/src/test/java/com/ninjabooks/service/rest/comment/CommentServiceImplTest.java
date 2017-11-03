package com.ninjabooks.service.rest.comment;

import com.ninjabooks.domain.Book;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentServiceImplTest
{
    private static final Supplier<Stream<Book>> BOOK_SUPPLIER = CommonUtils.asSupplier(DomainTestConstants.BOOK_FULL);
    private static final Supplier<Stream<Book>> EMPTY_SUPPLIER = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookDaoService bookDaoServiceMock;

    private CommentService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new CommentServiceImpl(bookDaoServiceMock);
    }

    @Test
    public void testFetchBookCommentsShouldReturnExpectedValues() throws Exception {
        when(bookDaoServiceMock.getByISBN(DomainTestConstants.ISBN)).thenReturn(BOOK_SUPPLIER.get());
        Set<CommentResponse> actual = sut.fetchBookComments(DomainTestConstants.ISBN);

        assertThat(actual)
            .extracting("author", "date", "content", "isbn")
            .containsExactly(
                tuple(
                    DomainTestConstants.NAME, DomainTestConstants.COMMENT_DATE, DomainTestConstants.COMMENT_CONTENT,
                    DomainTestConstants.ISBN));
    }

    @Test
    public void testFetchBookCommentsShouldReturnEmptyStreamWhenCommentsNotFound() throws Exception {
        when(bookDaoServiceMock.getByISBN(DomainTestConstants.ISBN)).thenReturn(EMPTY_SUPPLIER.get());
        Set<CommentResponse> actual = sut.fetchBookComments(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
    }
}
