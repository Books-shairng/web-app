package com.ninjabooks.json.notification;

import com.ninjabooks.domain.Queue;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.QueueDto;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueueNotificationTest
{
    private static final int POSITION_IN_QUEUE = 1;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private Queue queueMock;

    @Mock
    private ModelMapper modelMapperMock;

    @Mock
    private QueueDto queueDtoMock;

    @Mock
    private BookDto bookDtoMock;

    private QueueNotification sut;

    @Before
    public void setUp() throws Exception {
        when(modelMapperMock.map(any(), any())).thenReturn(bookDtoMock, queueDtoMock);
        this.sut = new QueueNotification(queueMock, POSITION_IN_QUEUE, modelMapperMock);
    }

    @Test
    public void testNotificationShouldReturnCorrectQueueReturnData() throws Exception {
        when(queueDtoMock.getOrderDate()).thenReturn(DomainTestConstants.ORDER_DATE.toString());
        QueueDto actual = sut.getQueueDto();

        assertThat(actual.getOrderDate()).isEqualTo(DomainTestConstants.ORDER_DATE.toString());

    }

    @Test
    public void testNotificationShouldReturnExpectedPositionInQueue() throws Exception {
        int actual = sut.getPositionInQueue();

        assertThat(actual).isEqualTo(POSITION_IN_QUEUE);
    }

    @Test
    public void testNotificationShouldReturnExpectedBook() throws Exception {
        when(bookDtoMock.getAuthor()).thenReturn(DomainTestConstants.AUTHOR);
        when(bookDtoMock.getTitle()).thenReturn(DomainTestConstants.TITLE);
        when(bookDtoMock.getIsbn()).thenReturn(DomainTestConstants.ISBN);
        BookDto actual = sut.getBookDto();

        assertSoftly(softly -> {
            assertThat(actual.getTitle()).isEqualTo(DomainTestConstants.TITLE);
            assertThat(actual.getAuthor()).isEqualTo(DomainTestConstants.AUTHOR);
            assertThat(actual.getIsbn()).isEqualTo(DomainTestConstants.ISBN);
        });
    }
}
