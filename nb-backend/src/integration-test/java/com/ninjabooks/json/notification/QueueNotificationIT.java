package com.ninjabooks.json.notification;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dto.BookDto;
import com.ninjabooks.dto.QueueDto;
import com.ninjabooks.util.constants.DomainTestConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class QueueNotificationIT
{
    private static final int POSITION_IN_QUEUE = 1;

    @Autowired
    private ModelMapper modelMapper;

    private QueueNotification sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new QueueNotification(DomainTestConstants.QUEUE_FULL, POSITION_IN_QUEUE, modelMapper);
    }

    @Test
    public void testNotificationShouldReturnCorrectQueueReturnData() throws Exception {
        QueueDto actual = sut.getQueueDto();

        assertThat(actual).extracting("orderDate")
            .contains(DomainTestConstants.ORDER_DATE);
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
            assertThat(actual.getTitle()).isEqualTo(DomainTestConstants.TITLE);
            assertThat(actual.getAuthor()).isEqualTo(DomainTestConstants.AUTHOR);
            assertThat(actual.getIsbn()).isEqualTo(DomainTestConstants.ISBN);
        });
    }
}
