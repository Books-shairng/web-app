package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBQueueDao;
import com.ninjabooks.domain.Queue;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBQueueDaoTest
{
    private static final LocalDateTime NEW_ORDER_DATE = LocalDateTime.now();
    private static final Supplier<Stream<Queue>> QUEUE_STREAM_SUPPLIER = CommonUtils.asSupplier(DomainTestConstants.QUEUE);
    private static final Supplier<Stream<Object>> EMPTY_STREAM_SUPPLIER = CommonUtils.asEmptySupplier();

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

    private QueueDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBQueueDao(sessionFactoryMock, specifiedElementFinderMock);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddQeueu() throws Exception {
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.QUEUE);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteQueue() throws Exception {
        doNothing().when(sessionMock).delete(DomainTestConstants.QUEUE);
        sut.delete(DomainTestConstants.QUEUE);

        verify(sessionMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testUpdateQueue() throws Exception {
        Queue beforeUpdate = createFreshEntity();
        beforeUpdate.setOrderDate(NEW_ORDER_DATE);
        doNothing().when(sessionMock).update(beforeUpdate);
        sut.update(beforeUpdate);

        verify(sessionMock, atLeastOnce()).update(any());
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        when(queryMock.stream()).thenReturn(QUEUE_STREAM_SUPPLIER.get());
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.QUEUE);
        verify(queryMock, atLeastOnce()).stream();
    }

    @Test
    public void testGetAllOnEmptyDBShouldReturnEmptyStream() throws Exception {
        Stream<Queue> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.QUEUE);
        Optional<Queue> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.QUEUE);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Queue> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetOrderByDate() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(QUEUE_STREAM_SUPPLIER.get());
        Stream<Queue> actual = sut.getByOrderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.QUEUE);
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    @Test
    public void testGetOrderByDateWhichNotExistShouldReturnEmptyStream() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(EMPTY_STREAM_SUPPLIER.get());
        Stream<Queue> actual = sut.getByOrderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).isEmpty();
        verify(specifiedElementFinderMock, atLeastOnce()).findSpecifiedElementInDB(any(), any(), any());
    }

    private Queue createFreshEntity() {
        return new Queue(DomainTestConstants.ORDER_DATE);
    }

}
