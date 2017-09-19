package com.ninjabooks.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 */
public class BorrowTest
{
    private static final LocalDate STANDARD_DATE = LocalDate.of(2017, 1, 1);
    private static final LocalDate STANDARD_RETURN_DATE = STANDARD_DATE.plusDays(30);
    private static final LocalDate DATE_MOVE_BY_NEXT_MONDAY = LocalDate.of(2017, 3, 3);
    private static final LocalDate RETURN_DATE_BY_NEXT_MONDAY = LocalDate.of(2017, 4, 3);
    private static final LocalDate EXTENDEND_DATE = LocalDate.now();

    private Borrow sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new Borrow();
    }

    @Test
    public void testShouldReturnCorrectReturnDate() throws Exception {
        sut.setBorrowDate(STANDARD_DATE);
        LocalDate actual = sut.getExpectedReturnDate();

        assertThat(actual).isEqualTo(STANDARD_RETURN_DATE);
    }

    @Test
    public void testWithSaturdayOrSundayAsReturnDayShouldMoveReturnDateToNextMonday() throws Exception {
        sut.setBorrowDate(DATE_MOVE_BY_NEXT_MONDAY);
        LocalDate actual = sut.getExpectedReturnDate();

        assertThat(actual).isEqualTo(RETURN_DATE_BY_NEXT_MONDAY);
        assertThat(actual.getDayOfWeek()).isNotIn(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    }

    @Test
    public void testExtendedReturnDateShouldMoveReturnDayByTwoWeeks() throws Exception {
        sut.setBorrowDate(EXTENDEND_DATE);
        sut.extendReturnDate();
        LocalDate actual = sut.getExpectedReturnDate();

        assertSoftly(softly -> {
            assertThat(actual).isAfterOrEqualTo(EXTENDEND_DATE.plusWeeks(2));
            assertThat(actual.getDayOfWeek()).isNotIn(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        });

    }
}
