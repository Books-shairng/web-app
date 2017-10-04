package com.ninjabooks.service.dao.borrow;

import com.ninjabooks.domain.Borrow;
import com.ninjabooks.service.dao.generic.GenericService;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BorrowService extends GenericService<Borrow, Long>
{
    /**
     * Find borrow date by specified data.
     *
     * @param borrowDate is parameter which is searched
     * @return desired borrow date
     */

    Stream<Borrow> getByBorrowDate(LocalDate borrowDate);

    /**
     * Find return date by specified data.
     *
     * @param returnDate is parameter which is searched
     * @return desired return date
     */

    Stream<Borrow> getByExpectedReturnDate(LocalDate returnDate);
}
