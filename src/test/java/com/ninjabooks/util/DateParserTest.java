package com.ninjabooks.util;

import org.junit.Test;

import java.time.LocalDate;

import static com.ninjabooks.util.DateParser.parseStringToLcoalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DateParserTest
{
    private final LocalDate EXPECTED_DATE = LocalDate.of(2017, 2, 2);

    @Test
    public void testParseStringToLocalDateWithDashSeparatorShouldSucceed() throws Exception {
        String date = "2017-2-2";

        LocalDate actual = parseStringToLcoalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testParseStringToDateWithWrongArgumentsShouldThrowsException() throws Exception {
        String date = "abcada";

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> parseStringToLcoalDate(date))
            .withMessageContaining("illegal")
            .withNoCause();
    }

    @Test
    public void testParseStringToDateWithMonthAndDayLessThenTenShouldSucceed() throws Exception {
        String date = "2017-02-02";

        LocalDate actual = parseStringToLcoalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testParseStringToLocalDateWithComaSeparatorShouldSucceed() throws Exception {
        String date = "2017,2,2";

        LocalDate actual = parseStringToLcoalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

    @Test
    public void testParseStringToLocalDateWithDotSeparatorShouldSucceed() throws Exception {
        String date = "2017.2.2";

        LocalDate actual = parseStringToLcoalDate(date);

        assertThat(actual).isEqualTo(EXPECTED_DATE);
    }

}
