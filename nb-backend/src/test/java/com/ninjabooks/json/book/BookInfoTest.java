package com.ninjabooks.json.book;

import com.ninjabooks.dto.BookDto;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookInfoTest
{
    private static final BookDto BOOK_DTO = new ModelMapper().map(DomainTestConstants.BOOK, BookDto.class);
    private static final int EXPECTED_SIZE = 1;

    private BookInfo sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookInfo(DomainTestConstants.BOOK);
    }

    @Test
    public void testQueueSizeShouldReturnExpectedSize() throws Exception {
        int actual = sut.getQueueSize();

        assertThat(actual).isEqualTo(EXPECTED_SIZE);
    }

    @Test
    public void testBookDtoShouldContainsFieldsOfObject() throws Exception {
        BookDto actual = sut.getBookDto();

        assertThat(actual).isEqualToComparingFieldByField(BOOK_DTO);
    }
}
