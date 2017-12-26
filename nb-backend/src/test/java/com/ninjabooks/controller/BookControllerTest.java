package com.ninjabooks.controller;

import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.error.handler.BookControllerHandler;
import com.ninjabooks.error.handler.EntityNotFoundHandler;
import com.ninjabooks.json.book.BookInfo;
import com.ninjabooks.service.rest.book.BookRestService;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.persistence.EntityNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    private static final BookInfo BOOK_INFO_RESPONSE = new BookInfo(DomainTestConstants.BOOK);
    private static final String JSON =
        "{" +
            "\"title\":\"" + DomainTestConstants.TITLE + "\"," +
            "\"author\":\"" + DomainTestConstants.AUTHOR + "\"," +
            "\"isbn\":\"" + DomainTestConstants.ISBN + "\"" +
        "}";

    @Rule
    public MockitoRule mockitoJUnit = MockitoJUnit.rule().silent();

    @Mock
    private BookRestService bookRestServiceMock;

    @Mock
    private ObjectMapper objectMapperMock;

    @Mock
    private ObjectNode objectNodeMock;

    private MockMvc mockMvc;
    private BookController sut;

    @Before
    public void setUp() throws Exception {
        when(objectMapperMock.createObjectNode()).thenReturn(objectNodeMock);
        this.sut = new BookController(bookRestServiceMock, objectMapperMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(new BookControllerHandler(), new EntityNotFoundHandler())
            .build();
    }

    @Test
    public void testAddNewBookShouldReturnStatusCreated() throws Exception {
        when(bookRestServiceMock.addBook(DomainTestConstants.BOOK)).thenReturn(any());

        mockMvc.perform(post("/api/book/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated());

        verify(bookRestServiceMock, atLeastOnce()).addBook(any());
    }

    @Test
    public void testAddBookWithNotGeneratedQRCodeShouldFail() throws Exception {
        when(bookRestServiceMock.addBook(any())).thenThrow(QRCodeException.class);

        mockMvc.perform(post("/api/book/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());

        verify(bookRestServiceMock, atLeastOnce()).addBook(any());
    }

    @Test
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        when(bookRestServiceMock.getBookInfo(anyLong())).thenReturn(BOOK_INFO_RESPONSE);

        mockMvc.perform(get("/api/book/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());

        verify(bookRestServiceMock, atLeastOnce()).getBookInfo(anyLong());
    }

    @Test
    public void testGetDetailsBookInfoShouldFailWhenBookNotFound() throws Exception {
        doThrow(EntityNotFoundException.class).when(bookRestServiceMock).getBookInfo(anyLong());

        mockMvc.perform(get("/api/book/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(bookRestServiceMock, atLeastOnce()).getBookInfo(anyLong());
    }
}
