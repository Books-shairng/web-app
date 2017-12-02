package com.ninjabooks.service.rest.borrow;

import com.ninjabooks.domain.Book;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.queue.QueueService;
import com.ninjabooks.util.QueueUtils;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Optional;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class RentalHelperTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookDaoService bookDaoServiceMock;

    @Mock
    private QueueUtils queueUtilsMock;

    @Mock
    private QueueService queueServiceMock;

    private Session sessionMock = Mockito.mock(Session.class, RETURNS_DEEP_STUBS);

    private RentalHelper sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new RentalHelper(bookDaoServiceMock, queueUtilsMock, queueServiceMock);
    }

    @Test
    public void testIsBookBorrowedShouldReturnFalseWhenBookIsFree() throws Exception {
        boolean actual = sut.isBookBorrowed(DomainTestConstants.BOOK);

        assertThat(actual).isFalse();
    }

    @Test
    public void testIsNotBelongToOtherUserQueueShouldReturnTrueWhenNotBelong() throws Exception {
        boolean actual = sut.isNotBelongToOtherUserQueue(DomainTestConstants.BOOK, DomainTestConstants.USER);

        assertThat(actual).isTrue();
    }

    @Test
    public void testIsNotBelongToOtherUserQueueShouldReturnFalseWhenBelong() throws Exception {
        boolean actual = sut.isNotBelongToOtherUserQueue(DomainTestConstants.BOOK_FULL, DomainTestConstants.USER_FULL);

        assertThat(actual).isFalse();
    }

    @Test
    public void testFindBookByQRCodeShouldReturnFoundedBook() throws Exception {
        when(bookDaoServiceMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(CriteriaQuery.class)).uniqueResultOptional())
            .thenReturn(Optional.of(DomainTestConstants.BOOK));

        Book actual = sut.findBookByQRCode(DomainTestConstants.DATA);

        assertThat(actual).isEqualTo(DomainTestConstants.BOOK);
        verify(sessionMock, atLeastOnce()).createQuery(any(CriteriaQuery.class));
    }

    @Test
    public void testFindBookByQRCodeShouldThrowsExcpetionWhenBookNotFound() throws Exception {
        when(bookDaoServiceMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(CriteriaQuery.class)).uniqueResultOptional())
            .thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.findBookByQRCode(DomainTestConstants.DATA))
            .withNoCause();

        verify(sessionMock, atLeastOnce()).createQuery(any(CriteriaQuery.class));
    }
}
