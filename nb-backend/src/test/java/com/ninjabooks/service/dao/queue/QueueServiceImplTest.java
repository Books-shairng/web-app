package com.ninjabooks.service.dao.queue;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.QueueDao;
import com.ninjabooks.domain.Queue;
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
public class QueueServiceImplTest
{
    private static final Supplier<Stream<Queue>> EXPECTED_VALUE = CommonUtils.asSupplier(DomainTestConstants.QUEUE);
    private static final Supplier<Stream<Queue>> EMPTY_STREAM = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule =MockitoJUnit.rule().silent();

    @Mock
    private QueueDao queueDaoMock;

    @Mock
    private GenericDao<Queue, Long> genericDaoMock;

    private QueueService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new QueueServiceImpl(genericDaoMock, queueDaoMock);
    }

    @Test
    public void testGetByOrderDateShouldReturnStreamWithExpectedValue() throws Exception {
        when(queueDaoMock.getByOrderDate(DomainTestConstants.ORDER_DATE)).thenReturn(EXPECTED_VALUE.get());
        Stream<Queue> actual = sut.getByOderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).containsExactly(DomainTestConstants.QUEUE);
        verify(queueDaoMock, atLeastOnce()).getByOrderDate(any());
    }

    @Test
    public void testGetByOrderDateWhichNotExistShouldReturnEmptyStream() throws Exception {
        when(queueDaoMock.getByOrderDate(DomainTestConstants.ORDER_DATE)).thenReturn(EMPTY_STREAM.get());
        Stream<Queue> actual = sut.getByOderDate(DomainTestConstants.ORDER_DATE);

        assertThat(actual).isEmpty();
        verify(queueDaoMock, atLeastOnce()).getByOrderDate(any());
    }
}
