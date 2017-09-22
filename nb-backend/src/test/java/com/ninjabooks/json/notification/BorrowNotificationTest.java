package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Borrow;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.BorrowDto;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowNotificationTest
{
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Borrow borrowMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private BorrowDto borrowDtoMock;

    @Mock
    private BookDto bookDtoMock;

    private BorrowNotification sut;

    @Before
    public void setUp() throws Exception {
        when(modelMapperMock.map(any(), any())).thenReturn(bookDtoMock, borrowDtoMock);
        this.sut = new BorrowNotification(borrowMock, modelMapperMock);
    }

    @Test
    public void testNotificationShouldReturnCorrectReturnDate() throws Exception {
        when(borrowDtoMock.getExpectedReturnDate()).thenReturn(DomainTestConstants.EXPECTED_RETURN_DATE);
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("expectedReturnDate")
            .contains(DomainTestConstants.EXPECTED_RETURN_DATE);
    }

    @Test
    public void testNotificationShouldReturnCorrectBorrowDate() throws Exception {
        when(borrowDtoMock.getBorrowDate()).thenReturn(DomainTestConstants.BORROW_DATE);
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("borrowDate").contains(DomainTestConstants.BORROW_DATE);
    }

    @Test
    public void testNotificationShouldReturnExpectedBorrowStatus() throws Exception {
        when(borrowDtoMock.getCanExtendBorrow()).thenReturn(DomainTestConstants.CAN_EXTEND);
        BorrowDto actual = sut.getBorrowDto();

        assertThat(actual).extracting("canExtendBorrow").contains(DomainTestConstants.CAN_EXTEND);
    }
}
