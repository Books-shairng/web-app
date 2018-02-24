package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBBookDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.db.SpecifiedElementFinder;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBBookDaoTest
{
    private static final String UPDATED_TITLE = "New title";
    private static final Supplier<Stream<Book>> BOOK_STREAM_SUPPLIER = CommonUtils.asSupplier(BOOK);
    private static final Supplier<Stream<Book>> EMPTY_STREAM_SUPPLIER = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    @Mock
    private SpecifiedElementFinder specifiedElementFinderMock;

    private DBBookDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBBookDao(sessionFactoryMock, specifiedElementFinderMock);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddBook() throws Exception {
        when(sessionMock.save(any())).thenReturn(ID);
        sut.add(BOOK);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(sessionMock).delete(BOOK);
        sut.delete(BOOK);

        verify(sessionMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetByID() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(BOOK);
        Optional<Book> actual = sut.getById(ID);

        assertThat(actual).contains(BOOK);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdBookWhichNotExistShouldRetunEmptyOptional() throws Exception {
        Optional<Book> actual = sut.getById(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAllShouldRetrunAllRecords() throws Exception {
        when(queryMock.stream()).thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getAll();

        assertThat(actual).containsExactly(BOOK);
        verify(queryMock, atLeastOnce()).stream();
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Book> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBooksByTitle() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByTitle(TITLE);

        assertThat(actual).containsExactly(BOOK);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByAuthor(AUTHOR);

        assertThat(actual).containsExactly(BOOK);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBooksByISBN() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByISBN(ISBN);

        assertThat(actual).containsExactly(BOOK);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBookTitleWhichNotExistShouldBeEmpty() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByTitle(TITLE);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBookAuthorWhichNotExistShouldBeEmpty() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByAuthor(AUTHOR);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBookISBNWhichNotExistShouldBeEmpty() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByISBN(ISBN);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testUpdateBookByEnity() throws Exception {
        Book beforeUpdate = createFreshEntity();
        beforeUpdate.setTitle(UPDATED_TITLE);

        doNothing().when(sessionMock).update(beforeUpdate);
        sut.update(beforeUpdate);

        verify(sessionMock, atLeastOnce()).update(any());
    }

    private Book createFreshEntity() {
        return new Book(TITLE, AUTHOR, ISBN);
    }
}
