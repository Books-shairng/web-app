package com.ninjabooks.service.rest.borrow.returnb;

import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.service.dao.history.HistoryService;
import com.ninjabooks.service.rest.borrow.RentalHelper;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookReturnServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private HistoryService historyServiceMock;

    @Mock
    private RentalHelper rentalHelperMock;

    private BookReturnService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookReturnServiceImpl(historyServiceMock, rentalHelperMock);
        when(rentalHelperMock.findBookByQRCode(anyString())).thenReturn(DomainTestConstants.BOOK_FULL);
    }

    @Test
    public void testReturnBookWhichNotExistShouldThrowsException() throws Exception {
        doThrow(EntityNotFoundException.class).when(rentalHelperMock).findBookByQRCode(anyString());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.returnBook(DomainTestConstants.DATA))
            .withNoCause();

        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
    }

    @Test
    public void testReturnBookShouldThrowsExceptionWhenBookIsNotBorrowed() throws Exception {
        when(rentalHelperMock.isBookBorrowed(DomainTestConstants.BOOK)).thenReturn(false);

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.returnBook(DomainTestConstants.DATA))
            .withNoCause();

        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isBookBorrowed(any());
    }

    @Test
    public void testReturnBookShouldSucceed() throws Exception {
        when(rentalHelperMock.isBookBorrowed(DomainTestConstants.BOOK)).thenReturn(true);
        sut.returnBook(DomainTestConstants.DATA);

        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isBookBorrowed(any());
    }
}
