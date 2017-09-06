package com.ninjabooks.controller;

import com.ninjabooks.service.rest.notification.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class NotificationControllerTest
{
    private static final List RESPONSE_CONTENT = Collections.singletonList("TEST");
    private static final int USER_ID = 1;

    @Mock
    private NotificationService notificationServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        NotificationController sut = new NotificationController(notificationServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    @SuppressWarnings("unchecked casting")
    public void testUserNotificationFoundBorrowsShouldReturnJson() throws Exception {
        when(notificationServiceMock.findUserBorrows(anyLong())).thenReturn(RESPONSE_CONTENT);

        mockMvc.perform(get("/api/notification/{userID}", USER_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(notificationServiceMock, atLeastOnce()).findUserBorrows(anyLong());
    }

    @Test
    @SuppressWarnings("unchecked casting")
    public void testUserNotificationFoundQueuesShouldRentunJson() throws Exception {
        when(notificationServiceMock.findUserQueues(anyLong())).thenReturn(RESPONSE_CONTENT);

        mockMvc.perform(get("/api/notification/{userID}", USER_ID))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(notificationServiceMock, atLeastOnce()).findUserQueues(anyLong());
    }

    @Test
    public void testUserNotificationNotFoundAnyBorrowsAndQueuesShouldRetunStauts204() throws Exception {
        mockMvc.perform(get("/api/notification/{userID}", USER_ID))
            .andExpect(status().isNoContent());

        verify(notificationServiceMock, atLeastOnce()).findUserQueues(anyLong());
        verify(notificationServiceMock, atLeastOnce()).findUserBorrows(anyLong());
    }
}
