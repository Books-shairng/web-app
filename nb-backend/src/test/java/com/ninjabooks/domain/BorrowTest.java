package com.ninjabooks.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 */
public class BorrowTest
{
    private static final LocalDate STANDARD_DATE = LocalDate.of(2017, 1, 1);
    private static final LocalDate DATE_MOVE_BY_NEXT_MONDAY = LocalDate.of(2017, 3, 2);
    private static final LocalDate EXTENDEND_DATE = LocalDate.now();

    private Borrow sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new Borrow();
    }

    @Test
    public void testShouldReturnCorrectReturnDate() throws Exception {
        sut.setBorrowDate(STANDARD_DATE);
        LocalDate actual = sut.getReturnDate();

        assertThat(actual).isEqualTo(STANDARD_DATE.plusDays(30));
    }

    @Test
    public void testWithSaturdayOrSundayAsReturnDayShouldMoveReturnDateToNextMonday() throws Exception {
        sut.setBorrowDate(DATE_MOVE_BY_NEXT_MONDAY);
        LocalDate actual = sut.getReturnDate();

        assertThat(actual)
            .isEqualTo(DATE_MOVE_BY_NEXT_MONDAY.plusDays(30).with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
    }

    @Test
    public void testExtendedReturnDateShouldMoveReturnDayByTwoWeeks() throws Exception {
        sut.setBorrowDate(EXTENDEND_DATE);
        sut.extendReturnDate();

        LocalDate actual = sut.getReturnDate();

        assertThat(actual).isEqualTo(EXTENDEND_DATE.plusDays(14).with(TemporalAdjusters.next(DayOfWeek.MONDAY)));
    }
}
