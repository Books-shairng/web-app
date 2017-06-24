package com.ninjabooks.domain;

import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(actual).isEqualTo(date.plusDays(30));
    }

    @Test
    public void testWithSaturdayOrSundayAsReturnDayShouldMoveReturnDateToNextMonday() throws Exception {
        LocalDate date = LocalDate.of(2017, 3, 2);

        borrow = new Borrow(date);
        LocalDate actual = borrow.getReturnDate();

        assertThat(actual).isEqualTo(date.plusDays(30).with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
    }

    @Test
    public void testExtendedReturnDateShouldMoveReturnDayByTwoWeeks() throws Exception {
        LocalDate date = LocalDate.now();

        borrow = new Borrow(date);
        borrow.extendReturnDate();

        LocalDate actual = borrow.getReturnDate();

        assertThat(actual).isEqualTo(date.plusDays(14).with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
    }
}
