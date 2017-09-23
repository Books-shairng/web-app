package com.ninjabooks.controller;

import com.ninjabooks.service.rest.notification.NotificationService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
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
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final List RESPONSE_CONTENT = Collections.singletonList("TEST");
    private static final long USER_ID = DomainTestConstants.ID;

    @Mock
    private NotificationService notificationServiceMock;

    private MockMvc mockMvc;
    private NotificationController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new NotificationController(notificationServiceMock);
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
