package com.ninjabooks.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 *
 */
public class BorrowTest
{
    private Borrow borrow;

    @Test
    public void testShouldReturnCorrectReturnDate() throws Exception {
        LocalDate date = LocalDate.of(2017, 1, 1);
        borrow = new Borrow(date);
        LocalDate actual = borrow.getReturnDate();

        Assertions.assertThat(actual).isEqualTo(date.plusDays(30));
    }
}