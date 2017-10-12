package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBBorrowDao;
import com.ninjabooks.domain.Borrow;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBBorrowDaoTest
{
    private static final LocalDate UPDATED_BORROW_DATE = LocalDate.now();
    private static final Supplier<Stream<Borrow>> BORROW_STREAM_SUPPLIER =
        CommonUtils.asSupplier(DomainTestConstants.BORROW);
    private static final Supplier<Stream<Object>> EMTPY_STREAM_SUPPLIER = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    @Mock
    private SpecifiedElementFinder specifiedElementFinderMock;

    private BorrowDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBBorrowDao(sessionFactoryMock, specifiedElementFinderMock);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddBorrow() throws Exception {
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.BORROW);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteBorrow() throws Exception {
        doNothing().when(sessionMock).delete(DomainTestConstants.BORROW);
        sut.delete(DomainTestConstants.BORROW);

        verify(sessionMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetAllShouldReturnsAllRecords() throws Exception {
        when(queryMock.stream()).thenReturn(BORROW_STREAM_SUPPLIER.get());
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
        verify(queryMock, atLeastOnce()).stream();
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Borrow> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.BORROW);
        Optional<Borrow> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.BORROW);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdEntityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Borrow> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetReturnDate() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(BORROW_STREAM_SUPPLIER.get());
        Stream<Borrow> actual = sut.getByExpectedReturnDate(DomainTestConstants.EXPECTED_RETURN_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBorrowDate() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(BORROW_STREAM_SUPPLIER.get());
        Stream<Borrow> actual = sut.getByBorrowDate(DomainTestConstants.BORROW_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.BORROW);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetReturnDateWhichNotExistShouldReturnsEmptyStream() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(EMTPY_STREAM_SUPPLIER.get());
        Stream<Borrow> actual = sut.getByExpectedReturnDate(DomainTestConstants.EXPECTED_RETURN_DATE);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetBorrowDateWhichNotExistShouldReturnEmtpyStream() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any()))
            .thenReturn(EMTPY_STREAM_SUPPLIER.get());
        Stream<Borrow> actual = sut.getByBorrowDate(DomainTestConstants.BORROW_DATE);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testUpdateBorrow() throws Exception {
        Borrow beforeUpdate = createFreshEntity();
        beforeUpdate.setBorrowDate(UPDATED_BORROW_DATE);

        doNothing().when(sessionMock).update(beforeUpdate);
        sut.update(beforeUpdate);

        verify(sessionMock, atLeastOnce()).update(any());
    }

    private Borrow createFreshEntity() {
        return new Borrow(DomainTestConstants.BORROW_DATE);
    }
}
