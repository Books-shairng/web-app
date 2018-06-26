package com.ninjabooks.controller;

import com.ninjabooks.error.exception.order.OrderException;
import com.ninjabooks.error.handler.OrderControllerHandler;
import com.ninjabooks.service.rest.order.OrderBookService;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.util.tests.HttpRequest;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class OrderControllerTest extends BaseUTController
{
    public static final String ID = String.valueOf(DomainTestConstants.ID);
    private static final String URL = "/api/order/{userID}/";

    @Mock
    private OrderBookService orderBookServiceMock;

    private OrderController sut;

    @Before
    public void setUp() throws Exception {
        sut = new OrderController(orderBookServiceMock);
        mockMvc(standaloneSetup(sut).setControllerAdvice(new OrderControllerHandler()));
    }

    @Test
    public void testOrderBookShouldReturnStatusOk() throws Exception {
        Map<String, List<String>> params = singletonMap("bookID", singletonList(ID));
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withUrlVars(ID)
            .withParameter("bookID", ID)
            .build());
    }

    @Test
    public void testOrderBookShouldReturnBadRequestStatusWhenUnableToCreateQueue() throws Exception {
        doThrow(OrderException.class).when(orderBookServiceMock).orderBook(anyLong(), anyLong());

        Map<String, List<String>> params = singletonMap("bookID", singletonList(ID));
        doPost(new HttpRequest.HttpRequestBuilder(URL)
            .withParameter("bookID", ID)
            .withUrlVars(ID)
            .withStatus(BAD_REQUEST)
            .build());

        verify(orderBookServiceMock, atLeastOnce()).orderBook(anyLong(), anyLong());
    }
}
