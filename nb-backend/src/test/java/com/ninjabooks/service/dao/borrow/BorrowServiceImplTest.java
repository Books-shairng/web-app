package com.ninjabooks.service.dao.borrow;

import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.CommonUtils;

import static com.ninjabooks.util.constants.DomainTestConstants.BORROW;
import static com.ninjabooks.util.constants.DomainTestConstants.BORROW_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;

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
public class BorrowServiceImplTest
{
    private static final Supplier<Stream<Borrow>> EXPTECTED_STREAM_BORROW = CommonUtils.asSupplier(BORROW);
    private static final Supplier<Stream<Borrow>> EMPTY_STREAM = CommonUtils.asEmptySupplier();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private BorrowDao borrowDaoMock;

    @Mock
    private GenericDao<Borrow, Long> genericDaoMock;

    private BorrowService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BorrowServiceImpl(genericDaoMock, borrowDaoMock);
    }

    @Test
    public void testGeyByBorrowDateShouldReturnStreamWithExpectedBorrow() throws Exception {
        when(borrowDaoMock.getByBorrowDate(BORROW_DATE)).thenReturn(EXPTECTED_STREAM_BORROW.get());
        Stream<Borrow> actual = sut.getByBorrowDate(BORROW_DATE);

        assertThat(actual).extracting(borrow -> borrow).containsExactly(BORROW);
        verify(borrowDaoMock, atLeastOnce()).getByBorrowDate(any());
    }

    @Test
    public void testGetByExpectedReturnDateShouldReturnStreamWithExpectedBorrow() throws Exception {
        when(borrowDaoMock.getByExpectedReturnDate(EXPECTED_RETURN_DATE))
            .thenReturn(EXPTECTED_STREAM_BORROW.get());
        Stream<Borrow> actual = sut.getByExpectedReturnDate(EXPECTED_RETURN_DATE);

        assertThat(actual).extracting(borrow -> borrow).containsExactly(BORROW);
        verify(borrowDaoMock, atLeastOnce()).getByExpectedReturnDate(any());
    }

    @Test
    public void testGetByBorrowDateShouldReturnEmptyStreamWhenBorrowNotFound() throws Exception {
        when(borrowDaoMock.getByBorrowDate(BORROW_DATE)).thenReturn(EMPTY_STREAM.get());
        Stream<Borrow> actual = sut.getByBorrowDate(BORROW_DATE);

        assertThat(actual).isEmpty();
        verify(borrowDaoMock, atLeastOnce()).getByBorrowDate(any());
    }

    @Test
    public void testGetByExpectedReturnDateShouldReturnEmptyStreamWhenBorrowNotFound() throws Exception {
        when(borrowDaoMock.getByExpectedReturnDate(EXPECTED_RETURN_DATE)).thenReturn(EMPTY_STREAM.get());
        Stream<Borrow> actual = sut.getByExpectedReturnDate(EXPECTED_RETURN_DATE);

        assertThat(actual).isEmpty();
        verify(borrowDaoMock, atLeastOnce()).getByExpectedReturnDate(any());

    }
}
