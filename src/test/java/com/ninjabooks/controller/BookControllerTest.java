package com.ninjabooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjabooks.domain.Book;
import com.ninjabooks.error.handler.BookControllerHandler;
import com.ninjabooks.error.qrcode.QRCodeUnableToCreateException;
import com.ninjabooks.service.rest.book.BookRestService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
    private static final String AUTHOR ="J. Bloch";
    private static final String TITLE = "Effective Java";
    private static final String ISBN = "978-0321356680";
    private static final Book BOOK = new Book(TITLE, AUTHOR, ISBN);

    private static final String JSON =
        "{" +
            "\"title\":\""+ TITLE + "\"," +
            "\"author\":\""+ AUTHOR+ "\"," +
            "\"isbn\":\""+ ISBN+ "\"" +
        "}";

    @Mock
    private BookRestService bookServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ObjectMapper objectMapper = new ObjectMapper();
        BookController sut = new BookController(bookServiceMock, objectMapper);

        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setMessageConverters(new MappingJackson2HttpMessageConverter())
            .setControllerAdvice(new BookControllerHandler())
            .build();
    }

    @Test
    public void testAddNewBookShouldReturnStatusCreated() throws Exception {
        when(bookServiceMock.addBook(BOOK)).thenReturn(any());

        mockMvc.perform(post("/api/books")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated());

        verify(bookServiceMock, atLeastOnce()).addBook(any(Book.class));
    }

    @Test
    public void testAddBookWithNotGeneratedQRCodeShouldFail() throws Exception {
        when(bookServiceMock.addBook(BOOK)).thenThrow(QRCodeUnableToCreateException.class);

        mockMvc.perform(post("/api/books")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());

        verify(bookServiceMock, atLeastOnce()).addBook(any(Book.class));
    }

    @Test
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
//        when(bookServiceMock.getById(anyLong())).thenReturn(Optional.of(BOOK));

        mockMvc.perform(get("/api/books/{bookID}", 1L))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());

//        verify(bookServiceMock, atLeastOnce()).getById(anyLong());
    }
}
