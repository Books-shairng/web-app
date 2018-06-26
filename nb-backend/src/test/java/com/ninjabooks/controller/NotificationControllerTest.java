package com.ninjabooks.controller;

import com.ninjabooks.service.rest.notification.NotificationService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class NotificationControllerTest extends BaseUTController
{
    private static final List RESPONSE_CONTENT = singletonList("TEST");
    private static final String URL = "/api/notification/{userID}";

    @Mock
    private NotificationService notificationServiceMock;

    private NotificationController sut;

    @Before
    public void setUp() throws Exception {
        sut = new NotificationController(notificationServiceMock);
        mockMvc(standaloneSetup(sut));
    }

    @Test
    @SuppressWarnings("unchecked casting")
    public void testUserNotificationFoundBorrowsShouldReturnJson() throws Exception {
        when(notificationServiceMock.findUserBorrows(anyLong())).thenReturn(RESPONSE_CONTENT);

        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build());

        verify(notificationServiceMock, atLeastOnce()).findUserBorrows(anyLong());
    }

    @Test
    @SuppressWarnings("unchecked casting")
    public void testUserNotificationFoundQueuesShouldRentunJson() throws Exception {
        when(notificationServiceMock.findUserQueues(anyLong())).thenReturn(RESPONSE_CONTENT);

        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build());

        verify(notificationServiceMock, atLeastOnce()).findUserQueues(anyLong());
    }

    @Test
    public void testUserNotificationNotFoundAnyBorrowsAndQueuesShouldRetunStauts204() throws Exception {
        when(notificationServiceMock.findUserBorrows(anyLong())).thenReturn(RESPONSE_CONTENT);
        when(notificationServiceMock.findUserQueues(anyLong())).thenReturn(RESPONSE_CONTENT);

        doGet(new HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .build());


        verify(notificationServiceMock, atLeastOnce()).findUserQueues(anyLong());
        verify(notificationServiceMock, atLeastOnce()).findUserBorrows(anyLong());
    }
}
