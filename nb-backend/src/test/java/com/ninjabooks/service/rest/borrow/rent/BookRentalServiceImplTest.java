package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.error.borrow.BorrowMaximumLimitException;
import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.service.rest.borrow.RentalHelper;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookRentalServiceImplTest
{
    private static final int MAXIMUM_BORROW_LIMIT = 5;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BorrowService borrowServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private RentalHelper rentalHelperMock;

    private BookRentalService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookRentalServiceImpl(borrowServiceMock, userServiceMock,rentalHelperMock);
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.of(DomainTestConstants.USER));
        when(rentalHelperMock.findBookByQRCode(anyString())).thenReturn(createFreshEntity());
    }

    @Test
    public void testRentBookShouldSucceed() throws Exception {
        when(rentalHelperMock.isNotBelongToOtherUserQueue(any(), any())).thenReturn(true);

        sut.rentBook(DomainTestConstants.ID, DomainTestConstants.DATA);

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isNotBelongToOtherUserQueue(any(), any());
    }

    @Test
    public void testRentBookShouldThrowsExceptionWhenBookNotFound() throws Exception {
        doThrow(EntityNotFoundException.class).when(rentalHelperMock).findBookByQRCode(anyString());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.rentBook(DomainTestConstants.ID, DomainTestConstants.DATA))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
    }

    @Test
    public void testRentBookShouldThrowsExceptionWhenBookIsAlreadyBorrowed() throws Exception {
        when(rentalHelperMock.isBookBorrowed(any())).thenReturn(true);

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(DomainTestConstants.ID, DomainTestConstants.DATA))
            .withNoCause()
            .withMessageContaining("already borrowed");

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isBookBorrowed(any());
    }

    @Test
    public void testRentBookShouldThrowsExceptionWhenBookBelongToOtherUser() throws Exception {
        when(rentalHelperMock.isNotBelongToOtherUserQueue(any(), any())).thenReturn(false);

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(DomainTestConstants.ID, DomainTestConstants.DATA))
            .withNoCause()
            .withMessage("Unable to borrow book");

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isNotBelongToOtherUserQueue(any(), any());
    }

    @Test
    public void testRentBookShouldThrowsExcpetionWhenLimitIsExceeded() throws Exception {
        User userMock = Mockito.mock(User.class);
        List listMock = Mockito.mock(List.class);
        when(userServiceMock.getById(any())).thenReturn(Optional.of(userMock));
        when(userMock.getBorrows()).thenReturn(listMock);
        when(listMock.size()).thenReturn(MAXIMUM_BORROW_LIMIT);

        assertThatExceptionOfType(BorrowMaximumLimitException.class)
            .isThrownBy(() -> sut.rentBook(DomainTestConstants.ID, DomainTestConstants.DATA))
            .withNoCause()
            .withMessageContaining("exceeded the limit");

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(userMock, atLeastOnce()).getBorrows();
        verify(listMock, atLeastOnce()).size();
    }

    private Book createFreshEntity() {
        return new Book(DomainTestConstants.TITLE, DomainTestConstants.AUTHOR, DomainTestConstants.ISBN);
    }
}
