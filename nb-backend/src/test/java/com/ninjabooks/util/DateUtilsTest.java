package com.ninjabooks.util;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.ninjabooks.util.DateUtils.parseStringToLocalDate;
import static com.ninjabooks.util.DateUtils.parseStringToLocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class DateUtilsTest
{
    private static final LocalTime TIME = LocalTime.of(12, 12, 12);
    private static final LocalDate EXPECTED_DATE = LocalDate.of(2017, 2, 2);
    private static final LocalDateTime EXPECTED_DATE_TIME = LocalDateTime.of(EXPECTED_DATE, TIME);

    @Test
    public void testParseStringToLocalDateWithDashSeparatorShouldSucceed() throws Exception {
        String date = "2017-2-2";
        LocalDate actual = parseStringToLocalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testParseStringToDateWithWrongArgumentsShouldThrowsException() throws Exception {
        String date = "abcada";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> parseStringToLocalDate(date))
            .withMessageContaining("illegal")
            .withNoCause();
    }

    @Test
    public void testParseStringToDateWithMonthAndDayLessThenTenShouldSucceed() throws Exception {
        String date = "2017-02-02";
        LocalDate actual = parseStringToLocalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testParseStringToLocalDateWithComaSeparatorShouldSucceed() throws Exception {
        String date = "2017,2,2";
        LocalDate actual = parseStringToLocalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testParseStringToLocalDateWithDotSeparatorShouldSucceed() throws Exception {
        String date = "2017.2.2";
        LocalDate actual = parseStringToLocalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testConvertStringToLocalDateTimeWithCorrectDataShouldSucced() throws Exception {
        String dateWithTime = "2017-02-02T12:12:12";
        LocalDateTime actual = parseStringToLocalDateTime(dateWithTime);

        assertThat(actual).isEqualTo(EXPECTED_DATE_TIME);
    }

    @Test
    public void testConvertStringToLocalDateTimeWithoutDateShouldThrowsException() throws Exception {
        String date = "2017.02.02";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> parseStringToLocalDateTime(date))
            .withNoCause();
    }

    @Test
    public void testConvertStringToLocalDateTimeWithoutTimeShouldThrowsException() throws Exception {
        String time = "12:12:12";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> parseStringToLocalDateTime(time))
            .withNoCause();
    }

    @Test
    public void testConvertStringToLocalDateTimeWithWrongTimeSeperatorShouldThrowsException() throws Exception {
        String dateWithTime = "2017-02-02Z12:12:12";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> parseStringToLocalDateTime(dateWithTime))
            .withNoCause();
    }

}
