package com.ninjabooks.controller;

import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.error.handler.OrderControllerHandler;
import com.ninjabooks.service.rest.order.OrderBookService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderControllerTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private OrderBookService orderBookServiceMock;

    private MockMvc mockMvc;
    private OrderController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new OrderController(orderBookServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(new OrderControllerHandler())
            .build();
    }

    @Test
    public void testOrderBookShouldReturnStatusOk() throws Exception {
        mockMvc.perform(post("/api/order/{userID}/", DomainTestConstants.ID)
            .param("bookID", "1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testOrderBookShouldReturnBadRequestStatusWhenUnableToCreateQueue() throws Exception {
        doThrow(OrderException.class).when(orderBookServiceMock).orderBook(anyLong(), anyLong());

        mockMvc.perform(post("/api/order/{userID}/", DomainTestConstants.ID)
            .param("bookID", "1"))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(orderBookServiceMock, atLeastOnce()).orderBook(anyLong(), anyLong());
    }
}
