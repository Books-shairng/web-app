package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBDaoHelper;
import com.ninjabooks.dao.db.DBHistoryDao;
import com.ninjabooks.domain.History;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
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
public class DBHistoryDaoTest
{
    private static final LocalDate UPDATED_RETURN_DATE = LocalDate.now();
    private static final Supplier<Stream<History>> HISTORY_STREAM_SUPPLIER =
        CommonUtils.asSupplier(DomainTestConstants.HISTORY);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private DBDaoHelper<History> daoHelperMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    private HistoryDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBHistoryDao(sessionFactoryMock, daoHelperMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddHistory() throws Exception {
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.HISTORY);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteHistoryByEnity() throws Exception {
        doNothing().when(daoHelperMock).delete(DomainTestConstants.HISTORY);
        sut.delete(DomainTestConstants.HISTORY);

        verify(daoHelperMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.HISTORY);
        Optional<History> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.HISTORY);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<History> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testFindAllHistoriesShouldReturnsAllRecords() throws Exception {
        when(queryMock.stream()).thenReturn(HISTORY_STREAM_SUPPLIER.get());
        Stream<History> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.HISTORY);
    }

    @Test
    public void testFindAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    public void testUpdateHistoryByEntity() throws Exception {
        History historyBeforeUpdate = DomainTestConstants.HISTORY;
        historyBeforeUpdate.setReturnDate(UPDATED_RETURN_DATE);

        doNothing().when(daoHelperMock).update(historyBeforeUpdate);
        sut.update(historyBeforeUpdate);

        verify(daoHelperMock, atLeastOnce()).update(any());
    }

}
