package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBBookDao;
import com.ninjabooks.dao.db.DBDaoHelper;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.util.db.SpecifiedElementFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBBookDaoTest
{
    private static final String UPDATED_TITLE = "New title";
    private static final Supplier<Stream<Book>> BOOK_STREAM_SUPPLIER = CommonUtils.asSupplier(DomainTestConstants.BOOK);
    private static final Supplier<Stream<Book>> EMPTY_STREAM_SUPPLIER = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private DBDaoHelper<Book> daoHelperMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    @Mock
    private SpecifiedElementFinder specifiedElementFinderMock;

    private DBBookDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBBookDao(sessionFactoryMock, daoHelperMock, specifiedElementFinderMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddBook() throws Exception {
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.BOOK);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteBook() throws Exception {
        doNothing().when(daoHelperMock).delete(DomainTestConstants.BOOK);
        sut.delete(DomainTestConstants.BOOK);

        verify(daoHelperMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetByID() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.BOOK);
        Optional<Book> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.BOOK);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdBookWhichNotExistShouldRetunEmptyOptional() throws Exception {
        Optional<Book> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAllShouldRetrunAllRecords() throws Exception {
        when(queryMock.stream()).thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(queryMock, atLeastOnce()).stream();
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Book> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetBooksByTitle() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any()))
            .thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any());
    }

    @Test
    public void testGetBooksByAuthor() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any()))
            .thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any());
    }

    @Test
    public void testGetBooksByISBN() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any()))
            .thenReturn(BOOK_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).containsExactly(DomainTestConstants.BOOK);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any());
    }

    @Test
    public void testGetBookTitleWhichNotExistShouldBeEmpty() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any()))
            .thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByTitle(DomainTestConstants.TITLE);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any());
    }

    @Test
    public void testGetBookAuthorWhichNotExistShouldBeEmpty() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any()))
            .thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByAuthor(DomainTestConstants.AUTHOR);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any());
    }

    @Test
    public void testGetBookISBNWhichNotExistShouldBeEmpty() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any()))
            .thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Book> actual = sut.getByISBN(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any());
    }

    @Test
    public void testUpdateBookByEnity() throws Exception {
        Book beforeUpdate = DomainTestConstants.BOOK;
        beforeUpdate.setTitle(UPDATED_TITLE);

        doNothing().when(daoHelperMock).update(beforeUpdate);
        sut.update(beforeUpdate);

        verify(daoHelperMock, atLeastOnce()).update(any());
    }
}
