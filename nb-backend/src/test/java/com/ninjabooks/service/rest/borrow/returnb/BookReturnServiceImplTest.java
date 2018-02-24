package com.ninjabooks.service.rest.borrow.returnb;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.service.dao.history.HistoryService;
import com.ninjabooks.service.rest.borrow.RentalHelper;
import com.ninjabooks.util.constants.DomainTestConstants;

import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.DATA;

import javax.persistence.EntityNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookReturnServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private HistoryService historyServiceMock;

    @Mock
    private RentalHelper rentalHelperMock;

    private BookReturnService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookReturnServiceImpl(historyServiceMock, rentalHelperMock);
        when(rentalHelperMock.findBookByQRCode(anyString())).thenReturn(createFreshEntity());
    }

    @Test
    public void testReturnBookWhichNotExistShouldThrowsException() throws Exception {
        doThrow(EntityNotFoundException.class).when(rentalHelperMock).findBookByQRCode(anyString());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.returnBook(DATA))
            .withNoCause();

        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
    }

    @Test
    public void testReturnBookShouldThrowsExceptionWhenBookIsNotBorrowed() throws Exception {
        when(rentalHelperMock.isBookBorrowed(BOOK)).thenReturn(false);

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.returnBook(DATA))
            .withNoCause();

        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isBookBorrowed(any());
    }

    @Test
    public void testReturnBookShouldSucceed() throws Exception {
        when(rentalHelperMock.isBookBorrowed(any())).thenReturn(true);

        sut.returnBook(DATA);

        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isBookBorrowed(any());
        verify(historyServiceMock, atLeastOnce()).add(any());
    }

    private Book createFreshEntity() {
        Book book = new Book();
        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUser(DomainTestConstants.USER);
        book.setBorrow(borrow);

        return book;
    }
}
