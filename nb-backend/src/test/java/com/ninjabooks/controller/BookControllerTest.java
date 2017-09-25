package com.ninjabooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjabooks.domain.Book;
import com.ninjabooks.error.handler.BookControllerHandler;
import com.ninjabooks.error.qrcode.QRCodeUnableToCreateException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.rest.book.BookRestService;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookControllerTest
{
    private static final Optional<Book> BOOK_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.BOOK);
    private static final String JSON =
        "{" +
            "\"title\":\"" + DomainTestConstants.TITLE + "\"," +
            "\"author\":\"" + DomainTestConstants.AUTHOR + "\"," +
            "\"isbn\":\"" + DomainTestConstants.ISBN + "\"" +
            "}";

    @Mock
    private BookRestService bookRestServiceMock;

    @Mock
    private BookDaoService bookServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        BookController sut = new BookController(bookRestServiceMock, objectMapper, bookServiceMock);

        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(new BookControllerHandler())
            .build();
    }

    @Test
    public void testAddNewBookShouldReturnStatusCreated() throws Exception {
        when(bookRestServiceMock.addBook(DomainTestConstants.BOOK)).thenReturn(any());

        mockMvc.perform(post("/api/books/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated());

        verify(bookRestServiceMock, atLeastOnce()).addBook(any());
    }

    @Test
    public void testAddBookWithNotGeneratedQRCodeShouldFail() throws Exception {
        when(bookRestServiceMock.addBook(any())).thenThrow(QRCodeUnableToCreateException.class);

        mockMvc.perform(post("/api/books/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());

        verify(bookRestServiceMock, atLeastOnce()).addBook(any());
    }

    @Test
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        when(bookServiceMock.getById(anyLong())).thenReturn(BOOK_OPTIONAL);

        mockMvc.perform(get("/api/books/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());

        verify(bookServiceMock, atLeastOnce()).getById(anyLong());
    }
}
