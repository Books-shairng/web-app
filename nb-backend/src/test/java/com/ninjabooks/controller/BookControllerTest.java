package com.ninjabooks.controller;

import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.error.handler.BookControllerHandler;
import com.ninjabooks.error.handler.EntityNotFoundHandler;
import com.ninjabooks.json.book.BookInfo;
import com.ninjabooks.service.rest.book.BookRestService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.DESCRIPTION;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import javax.persistence.EntityNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookControllerTest extends BaseUTController
{
    private static final String URL = "/api/book/";
    private static final BookInfo BOOK_INFO_RESPONSE = new BookInfo(BOOK);
    private static final String JSON =
        "{\"title\":\"" + TITLE + "\",\"author\":\"" + AUTHOR + "\"," +
        "\"isbn\":\"" + ISBN + "\",\"description\":\"" + DESCRIPTION + "\"}";

    @Mock
    private BookRestService bookRestServiceMock;

    private BookController sut;

    @Before
    public void setUp() throws Exception {
        sut = new BookController(bookRestServiceMock, new ObjectMapper());
        mockMvc(standaloneSetup(sut).setControllerAdvice(new BookControllerHandler(), new EntityNotFoundHandler()));
    }

    @Test
    public void testAddNewBookShouldReturnStatusCreated() throws Exception {
        when(bookRestServiceMock.addBook(BOOK)).thenReturn(any());

        doPost(new HttpRequestBuilder(URL)
            .withContent(JSON)
            .withStatus(CREATED)
            .build());

        verify(bookRestServiceMock, atLeastOnce()).addBook(any());
    }

    @Test
    public void testAddBookWithNotGeneratedQRCodeShouldFail() throws Exception {
        when(bookRestServiceMock.addBook(any())).thenThrow(QRCodeException.class);

        doPost(new HttpRequestBuilder(URL)
            .withContent(JSON)
            .withStatus(BAD_REQUEST)
            .build());

        verify(bookRestServiceMock, atLeastOnce()).addBook(any());
    }

    @Test
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        when(bookRestServiceMock.getBookInfo(anyLong())).thenReturn(BOOK_INFO_RESPONSE);

        doGet(new HttpRequestBuilder(URL + "{bookID}")
            .withUrlVars(ID)
            .build());

        verify(bookRestServiceMock, atLeastOnce()).getBookInfo(anyLong());
    }

    @Test
    public void testGetDetailsBookInfoShouldFailWhenBookNotFound() throws Exception {
        doThrow(EntityNotFoundException.class).when(bookRestServiceMock).getBookInfo(anyLong());

        doGet(new HttpRequestBuilder(URL + "{bookID}")
            .withUrlVars(ID)
            .withStatus(BAD_REQUEST)
            .build());

        verify(bookRestServiceMock, atLeastOnce()).getBookInfo(anyLong());
    }
}
