package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.service.rest.borrow.RentalHelper;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookRentalServiceImplTest
{
    private static final int MAXIMUM_BORROW_LIMIT = 5;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private BorrowService borrowServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private RentalHelper rentalHelperMock;

    private BookRentalService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookRentalServiceImpl(borrowServiceMock, userServiceMock, rentalHelperMock);
        when(userServiceMock.getById(ID)).thenReturn(Optional.of(USER));
        when(rentalHelperMock.findBookByQRCode(anyString())).thenReturn(createFreshEntity());
    }

    @Test
    public void testRentBookShouldSucceed() throws Exception {
        when(rentalHelperMock.isNotBelongToOtherUserQueue(any(), any())).thenReturn(true);

        sut.rentBook(ID, DATA);

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
        verify(rentalHelperMock, atLeastOnce()).isNotBelongToOtherUserQueue(any(), any());
    }

    @Test
    public void testRentBookShouldThrowsExceptionWhenBookNotFound() throws Exception {
        doThrow(EntityNotFoundException.class).when(rentalHelperMock).findBookByQRCode(anyString());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(rentalHelperMock, atLeastOnce()).findBookByQRCode(anyString());
    }

    @Test
    public void testRentBookShouldThrowsExceptionWhenBookIsAlreadyBorrowed() throws Exception {
        when(rentalHelperMock.isBookBorrowed(any())).thenReturn(true);

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
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
            .isThrownBy(() -> sut.rentBook(ID, DATA))
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

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.rentBook(ID, DATA))
            .withNoCause()
            .withMessageContaining("exceeded the limit");

        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(userMock, atLeastOnce()).getBorrows();
        verify(listMock, atLeastOnce()).size();
    }

    private Book createFreshEntity() {
        return new Book(TITLE, AUTHOR, ISBN);
    }
}
