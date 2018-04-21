package com.ninjabooks.json.notification;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.QueueDto;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.ORDER_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.QUEUE_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QueueNotificationIT extends AbstractBaseIT
{
    private static final int POSITION_IN_QUEUE = 1;

    @Autowired
    private ModelMapper modelMapper;

    private QueueNotification sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new QueueNotification(QUEUE_FULL, POSITION_IN_QUEUE, modelMapper);
    }

    @Test
    public void testNotificationShouldReturnCorrectQueueReturnData() throws Exception {
        QueueDto actual = sut.getQueueDto();

        assertThat(actual).extracting("orderDate")
            .contains(ORDER_DATE);
    }

    @Test
    public void testNotificationShouldReturnExpectedPositionInQueue() throws Exception {
        int actual = sut.getPositionInQueue();

        assertThat(actual).isEqualTo(POSITION_IN_QUEUE);
    }

    @Test
    public void testNotificationShouldReturnExpectedBook() throws Exception {
        BookDto actual = sut.getBookDto();

        assertSoftly(softly -> {
            assertThat(actual.getTitle()).isEqualTo(TITLE);
            assertThat(actual.getAuthor()).isEqualTo(AUTHOR);
            assertThat(actual.getIsbn()).isEqualTo(ISBN);
        });
    }
}
