package com.ninjabooks.json.notification;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowNotificationTest
{
    @Mock
    private BorrowNotification queueNotification;

    private static final String AUTHOR = "C. Ho, R. Harrop, C. Schaefer";
    private static final String TITLE = "Pro Spring, 4th Edition";
    private static final String ISBN = "978-1430261513";
    private static final String DATE = "2017-05-03";

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNotificationReturnCorrectData() throws Exception {

        when(queueNotification.getAuthor()).thenReturn(AUTHOR);
        when(queueNotification.getTitle()).thenReturn(TITLE);
        when(queueNotification.getIsbn()).thenReturn(ISBN);
        when(queueNotification.getReturnDate()).thenReturn(DATE);
        when(queueNotification.getBorrowDate()).thenReturn(DATE);


        assertSoftly(softly -> {
            assertThat(queueNotification.getAuthor()).isEqualTo(AUTHOR);
            assertThat(queueNotification.getIsbn()).isEqualTo(ISBN);
            assertThat(queueNotification.getTitle()).isEqualTo(TITLE);
            assertThat(queueNotification.getReturnDate()).isEqualTo(DATE);
            assertThat(queueNotification.getBorrowDate()).isEqualTo(DATE);
        });
    }
}
