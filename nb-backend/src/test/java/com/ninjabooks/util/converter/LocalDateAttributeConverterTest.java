package com.ninjabooks.util.converter;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class LocalDateAttributeConverterTest
{
    private static final LocalDate LOCAL_DATE = LocalDate.of(2017,1,1);
    private static final Date DATE = Date.valueOf(LOCAL_DATE);

    private LocalDateAttributeConverter sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new LocalDateAttributeConverter();
    }

    @Test
    public void testConvertLocalDateShouldReturnProperSQLDate() throws Exception {
        Date actual = sut.convertToDatabaseColumn(LOCAL_DATE);

        assertThat(actual).isEqualTo(DATE);
    }

    @Test
    public void testConvertDateToLocalDateShouldReturnProperLocalDate() throws Exception {
        LocalDate actual = sut.convertToEntityAttribute(DATE);

        assertThat(actual).isEqualTo(LOCAL_DATE);
    }
}
