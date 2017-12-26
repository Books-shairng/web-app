package com.ninjabooks.service.dao.book;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookDaoServiceImplTest
{
    private static final Supplier<Stream<Book>> EXPECTED_BOOK = CommonUtils.asSupplier(DomainTestConstants.BOOK);
    private static final Supplier<Stream<Book>> EMPTY_STREAM = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule =MockitoJUnit.rule().silent();

    @Mock
    private BookDao bookDaoMock;

    @Mock
    private GenericDao<Book, Long> genericDaoMock;

    private BookDaoServiceImpl sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookDaoServiceImpl(genericDaoMock, bookDaoMock);
    }

    @Test
    public void testGetByTitleShouldReturnsExpectedStream() throws Exception {
        when(bookDaoMock.getByTitle(DomainTestConstants.TITLE)).thenReturn(EXPECTED_BOOK.get());
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(bookDaoMock, atLeastOnce()).getByTitle(any());
    }

    @Test
    public void testGetByAuthorShouldReturnsExpectedStream() throws Exception {
        when(bookDaoMock.getByAuthor(DomainTestConstants.AUTHOR)).thenReturn(EXPECTED_BOOK.get());
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(bookDaoMock, atLeastOnce()).getByAuthor(any());
    }

    @Test
    public void testGetByISBNShouldReturnsExpectedStream() throws Exception {
        when(bookDaoMock.getByISBN(DomainTestConstants.ISBN)).thenReturn(EXPECTED_BOOK.get());
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(bookDaoMock, atLeastOnce()).getByISBN(any());
    }

    @Test
    public void testGetByTitleWhichNotExistShouldReturnEmptyStream() throws Exception {
        when(bookDaoMock.getByTitle(DomainTestConstants.TITLE)).thenReturn(EMPTY_STREAM.get());
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).isEmpty();
        verify(bookDaoMock, atLeastOnce()).getByTitle(any());
    }

    @Test
    public void testGetByAuthorWhichNotExistShouldReturnEmptyStream() throws Exception {
        when(bookDaoMock.getByAuthor(DomainTestConstants.AUTHOR)).thenReturn(EMPTY_STREAM.get());
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).isEmpty();
        verify(bookDaoMock, atLeastOnce()).getByAuthor(any());
    }

    @Test
    public void testGetByISBNWhichNotExistShouldReturnEmptyStream() throws Exception {
        when(bookDaoMock.getByISBN(DomainTestConstants.ISBN)).thenReturn(EMPTY_STREAM.get());
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
        verify(bookDaoMock, atLeastOnce()).getByISBN(any());
    }
}
