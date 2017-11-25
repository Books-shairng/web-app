package com.ninjabooks.json.history;

import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.HistoryDto;
import com.ninjabooks.util.constants.DomainTestConstants;
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
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private UserHistoryResponse sut;

    @Before
    public void setUp() throws Exception {
        HistoryDto historyDto = createDtoObject(HistoryDto.class, DomainTestConstants.HISTORY_FULL);
        BookDto bookDto = createDtoObject(BookDto.class, DomainTestConstants.BOOK_FULL);
        this.sut = new UserHistoryResponse(historyDto, bookDto);
    }

    @Test
    public void testUserHistoryResponseShouldReturnExpectedReturnDate() throws Exception {
        HistoryDto actual = sut.getHistoryDto();

        assertThat(actual).extracting("returnDate").containsExactly(DomainTestConstants.EXPECTED_RETURN_DATE);
    }

    @Test
    public void testUserHistoryResponseShouldReturnExpectedBook() throws Exception {
        BookDto actual = sut.getBookDto();

        assertSoftly(softly -> {
                  assertThat(actual.getAuthor()).isEqualTo(DomainTestConstants.AUTHOR);
                  assertThat(actual.getIsbn()).isEqualTo(DomainTestConstants.ISBN);
                  assertThat(actual.getTitle()).isEqualTo(DomainTestConstants.TITLE);
        });
    }

    private <D, E> D createDtoObject(Class<D> dtoClass, E sourceEnity) {
        return new ModelMapper().map(sourceEnity, dtoClass);
    }
}
