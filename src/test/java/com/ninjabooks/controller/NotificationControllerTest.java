package com.ninjabooks.controller;

import com.ninjabooks.service.NotificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class NotificationControllerTest
{
    @Mock
    private NotificationService notificationServiceMock;

    @InjectMocks
    private NotificationController notificationControllerMock;

    private MockMvc mockMvc;
    private List list;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(notificationControllerMock).build();
        this.list = new ArrayList<>();
        list.add("TEST");
    }

    @Test
    public void testUserNotificationFoundBorrowsShouldReturnJson() throws Exception {
        when(notificationServiceMock.findUserBorrows(anyLong())).thenReturn(list);
        mockMvc.perform(get("/api/notification/{userID}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(notificationServiceMock, times(1)).findUserBorrows(anyLong());
    }

    @Test
    public void testUserNotificationFoundQueuesShouldRentunJson() throws Exception {
        when(notificationServiceMock.findUserQueues(anyLong())).thenReturn(list);
        mockMvc.perform(get("/api/notification/{userID}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(notificationServiceMock, times(1)).findUserQueues(anyLong());
    }

    @Test
    public void testUserNotificationNotFoundAnyBorrowsAndQueuesShouldRetunStauts204() throws Exception {
        mockMvc.perform(get("/api/notification/{userID}", 2))
            .andExpect(status().isNoContent());

        verify(notificationServiceMock, times(1)).findUserQueues(anyLong());
        verify(notificationServiceMock, times(1)).findUserBorrows(anyLong());
    }
}
