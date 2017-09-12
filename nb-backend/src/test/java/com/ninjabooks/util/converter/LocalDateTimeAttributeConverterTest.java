package com.ninjabooks.util.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class LocalDateTimeAttributeConverterTest
{
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2017, 1, 1, 1, 1);
    private static final Timestamp TIMESTAMP = new Timestamp(1483228860000L);

    private LocalDateTimeAttributeConverter sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new LocalDateTimeAttributeConverter();
    }

    @Test
    public void testConvertLocalDateTimeShouldReturnProperTimestamp() throws Exception {
        Timestamp actual = sut.convertToDatabaseColumn(DATE_TIME);

        assertThat(actual).isEqualTo(TIMESTAMP);
    }

    @Test
    public void testConvertTimestampShouldReturnExpectreDateTime() throws Exception {
        LocalDateTime actual = sut.convertToEntityAttribute(TIMESTAMP);

        assertThat(actual).isEqualTo(DATE_TIME);
    }
}
