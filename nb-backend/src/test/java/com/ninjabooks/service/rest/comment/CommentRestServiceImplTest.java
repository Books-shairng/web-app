package com.ninjabooks.service.rest.comment;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.History;
import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.comment.CommentDaoService;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.util.entity.EntityUtilsWrapper;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentRestServiceImplTest
{
    private static final Supplier<Stream<Book>> BOOK_SUPPLIER = CommonUtils.asSupplier(DomainTestConstants.BOOK_FULL);
    private static final Supplier<Stream<Book>> EMPTY_SUPPLIER = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookDaoService bookDaoServiceMock;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CommentDaoService commentDaoServiceMock;

    @Mock
    private EntityUtilsWrapper entityUtilsWrapperMock;

    @Mock
    private Query queryMock;

    private CommentRestService sut;

    @Before
    public void setUp() throws Exception {
        when(commentDaoServiceMock.getSession().createQuery(anyString(), any())).thenReturn(queryMock);
        this.sut = new CommentRestServiceImpl(bookDaoServiceMock, commentDaoServiceMock, entityUtilsWrapperMock);
    }

    @Test
    public void testFetchBookCommentsShouldReturnExpectedValues() throws Exception {
        when(bookDaoServiceMock.getByISBN(DomainTestConstants.ISBN)).thenReturn(BOOK_SUPPLIER.get());
        Set<CommentResponse> actual = sut.getComments(DomainTestConstants.ISBN);

        assertThat(actual)
            .extracting("author", "date", "content", "isbn")
            .containsExactly(
                tuple(
                    DomainTestConstants.NAME, DomainTestConstants.COMMENT_DATE, DomainTestConstants.COMMENT_CONTENT,
                    DomainTestConstants.ISBN));
        verify(bookDaoServiceMock, atLeastOnce()).getByISBN(any());
    }

    @Test
    public void testFetchBookCommentsShouldReturnEmptyStreamWhenCommentsNotFound() throws Exception {
        when(bookDaoServiceMock.getByISBN(DomainTestConstants.ISBN)).thenReturn(EMPTY_SUPPLIER.get());
        Set<CommentResponse> actual = sut.getComments(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
        verify(bookDaoServiceMock, atLeastOnce()).getByISBN(any());
    }

    @Test
    public void testAddCommentShouldSucceed() throws Exception {
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.of(createFreshEnity(false, true)));
        when(entityUtilsWrapperMock.getEnity((Class<BaseEntity>) any(), any()))
            .thenReturn(DomainTestConstants.USER).thenReturn(DomainTestConstants.BOOK);

        sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID);

        verify(commentDaoServiceMock, atLeastOnce()).getSession();
        verify(queryMock, atLeastOnce()).uniqueResultOptional();
        verify(entityUtilsWrapperMock, atLeastOnce()).getEnity((Class<BaseEntity>) any(), any());
    }


    @Test
    public void testAddCommentShouldThrowsExceptionWhenBookWasAlreadyCommented() throws Exception {
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.of(createFreshEnity(true, false)));

        assertThatExceptionOfType(CommentException.class)
            .isThrownBy(() ->
                sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessage("Unable to add new comment");

        verify(commentDaoServiceMock, atLeastOnce()).getSession();
        verify(queryMock, atLeastOnce()).uniqueResultOptional();
    }

    @Test
    public void testAddCommentShouldThrowsExceptionWhenDateIsOverdue() throws Exception {
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.of(createFreshEnity(false, false)));

        assertThatExceptionOfType(CommentException.class)
            .isThrownBy(() ->
                sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessage("Unable to add new comment");

        verify(commentDaoServiceMock, atLeastOnce()).getSession();
        verify(queryMock, atLeastOnce()).uniqueResultOptional();
    }

    @Test
    public void testAddChommentShouldThrowsExceptionHistoryEntityNotFound() throws Exception {
        when(queryMock.uniqueResultOptional()).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() ->
                sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessageContaining("enity not found");

        verify(commentDaoServiceMock, atLeastOnce()).getSession();
        verify(queryMock, atLeastOnce()).uniqueResultOptional();
    }

    private History createFreshEnity(boolean isCommented, boolean isActaulDate) {
        History history = new History();
        history.setIsActive(isCommented);
        history.setReturnDate(isActaulDate ? LocalDate.now().minusDays(1) : LocalDate.now().plusMonths(1));

        return history;
    }
}
