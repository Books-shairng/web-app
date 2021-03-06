package com.ninjabooks.json.history;

import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.HistoryDto;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.EXPECTED_RETURN_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.HISTORY_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserHistoryResponseTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    private UserHistoryResponse sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserHistoryResponse(HISTORY_FULL, new ModelMapper());
    }

    @Test
    public void testUserHistoryResponseShouldReturnExpectedReturnDate() throws Exception {
        HistoryDto actual = sut.getHistoryDto();

        assertThat(actual).extracting("returnDate").containsExactly(EXPECTED_RETURN_DATE);
    }

    @Test
    public void testUserHistoryResponseShouldReturnExpectedBook() throws Exception {
        BookDto actual = sut.getBookDto();

        assertSoftly(softly -> {
            assertThat(actual.getAuthor()).isEqualTo(AUTHOR);
            assertThat(actual.getIsbn()).isEqualTo(ISBN);
            assertThat(actual.getTitle()).isEqualTo(TITLE);
        });
    }

}
