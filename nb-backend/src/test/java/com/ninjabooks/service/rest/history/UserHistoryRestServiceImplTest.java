package com.ninjabooks.service.rest.history;

import com.ninjabooks.domain.User;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.HistoryDto;
import com.ninjabooks.json.history.GenericHistoryResponse;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class UserHistoryRestServiceImplTest
{
    private static final Optional<User> USER_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.USER_FULL);
    private static final long MINUS_NUMBER_OF_DAY = 10L;
    private static final long MINUS_ZERO_DAY = 0L;
    private static final int EXPECTED_SIZE = 1;

    @Rule
    public MockitoRule mockitoRule =MockitoJUnit.rule().silent();

    @Mock
    private UserService userServiceMock;

    @Mock
    private ModelMapper modelMapperMock;

    private HistoryRestService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new UserHistoryRestServiceImpl(userServiceMock, modelMapperMock);
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(USER_OPTIONAL);
    }

    @Test
    public void testGetHistoryShouldReturnListWithExpectedSize() throws Exception {
        when(modelMapperMock.map(any(), any())).thenReturn(initHistoryDto()).thenReturn(initBookDto());
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID);

        assertThat(actual).hasSize(EXPECTED_SIZE);
        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(modelMapperMock, atLeastOnce()).map(any(), any());
    }

    @Test
    public void testGetHistoryShouldReturnExpectedDtoFields() throws Exception {
        when(modelMapperMock.map(any(), any())).thenReturn(initHistoryDto()).thenReturn(initBookDto());
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID);

        assertThat(actual)
            .extracting("historyDto.returnDate", "bookDto.author", "bookDto.isbn", "bookDto.title")
            .containsExactly(tuple(
                DomainTestConstants.EXPECTED_RETURN_DATE,
                DomainTestConstants.AUTHOR,
                DomainTestConstants.ISBN,
                DomainTestConstants.TITLE));
        verify(userServiceMock, atLeastOnce()).getById(any());
        verify(modelMapperMock, atLeastOnce()).map(any(), any());
    }

    @Test
    public void testGetHistoryShouldReturnEmptyListWhenUserDontHaveAnyHistory() throws Exception {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.ofNullable(DomainTestConstants.USER));
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID);

        assertThat(actual).isEmpty();
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetHistoryShouldReturnEmptyListWhenMinusDayIsLarge() throws Exception {
        when(modelMapperMock.map(any(), any())).thenReturn(initHistoryDto()).thenReturn(initBookDto());
        List<GenericHistoryResponse> actual = sut.getHistory(MINUS_NUMBER_OF_DAY, DomainTestConstants.ID);

        assertThat(actual).isEmpty();
        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    @Test
    public void testGetHistoryShouldThrowsExceptionWhenUnableToFindUser() throws Exception {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> sut.getHistory(MINUS_ZERO_DAY, DomainTestConstants.ID))
            .withNoCause();

        verify(userServiceMock, atLeastOnce()).getById(any());
    }

    private HistoryDto initHistoryDto() {
        HistoryDto historyDto = new HistoryDto();
        historyDto.setReturnDate(DomainTestConstants.EXPECTED_RETURN_DATE);

        return historyDto;
    }

    private BookDto initBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setAuthor(DomainTestConstants.AUTHOR);
        bookDto.setIsbn(DomainTestConstants.ISBN);
        bookDto.setTitle(DomainTestConstants.TITLE);

        return bookDto;
    }
}
