package com.ninjabooks.service.rest.borrow.extend;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ExtendRentalServiceImplTest
{
    private static final LocalDate ACTUAL_DATE_PLUS_TWO_WEEKS = LocalDate.now().plusWeeks(2);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookDaoService bookDaoService;

    private Book BOOK_ENTITY;
    private ExtendRentalService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new ExtendRentalServiceImpl(bookDaoService);
        BOOK_ENTITY = createFreshEnity();
    }

    @Test
    public void testExtendReturnDateShouldReturnExpectedStatus() throws Exception {
        when(bookDaoService.getById(DomainTestConstants.ID)).thenReturn(Optional.of(BOOK_ENTITY));

        sut.extendReturnDate(DomainTestConstants.ID, DomainTestConstants.ID);

        assertThat(BOOK_ENTITY).extracting("borrow.canExtendBorrow").contains(false);
        verify(bookDaoService, atLeastOnce()).getById(any());
    }

    @Test
    public void testExtendReturnDateShouldExtendReturnDateByTwoWeeks() throws Exception {
        when(bookDaoService.getById(DomainTestConstants.ID)).thenReturn(Optional.of(BOOK_ENTITY));

        sut.extendReturnDate(DomainTestConstants.ID, DomainTestConstants.ID);
        Borrow actual = BOOK_ENTITY.getBorrow();

        assertThat(actual.getExpectedReturnDate()).isAfterOrEqualTo(ACTUAL_DATE_PLUS_TWO_WEEKS);
        verify(bookDaoService, atLeastOnce()).getById(any());
    }

    @Test
    public void testExtendReturnDateShouldThrowsExceptionWhenBookNotExist() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.extendReturnDate(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause();
    }

    @Test
    public void testEtendReturnDateShouldThrowsExceptionWhenBookIsNotBorrowed() throws Exception {
        when(bookDaoService.getById(DomainTestConstants.ID)).thenReturn(Optional.of(BOOK_ENTITY));
        BOOK_ENTITY.setBorrow(null);

        assertThatExceptionOfType(BorrowException.class)
            .isThrownBy(() -> sut.extendReturnDate(DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessageContaining("is not borrowed");

        verify(bookDaoService, atLeastOnce()).getById(anyLong());
    }

    private Book createFreshEnity() {
        Book book = new Book(DomainTestConstants.TITLE, DomainTestConstants.AUTHOR, DomainTestConstants.ISBN);
        Borrow borrow = new Borrow(LocalDate.now());
        borrow.setUser(DomainTestConstants.USER);
        book.setStatus(BookStatus.BORROWED);
        book.setBorrow(borrow);

        return book;
    }
}
