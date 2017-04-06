package com.ninjabooks.dao;

import com.ninjabooks.domain.Borrow;

import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public interface BorrowDao extends GenericDao<Borrow, Long>
{
    Borrow getByBorrowDate(LocalDate borrowDate);
    Borrow getByReturnDate(LocalDate returnDate);
}
