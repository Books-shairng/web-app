package com.ninjabooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjabooks.domain.Book;
import com.ninjabooks.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
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

    private final Book book = new Book(TITLE, AUTHOR, ISBN);

    private final String json =
        "{" +
            "\"title\":\""+ TITLE + "\"," +
            "\"author\":\""+ AUTHOR+ "\"," +
            "\"isbn\":\""+ ISBN+ "\"" +
        "}";
    @Mock
    private BookService bookServiceMock;
    private ObjectMapper objectMapperMock;
    private BookController bookControllerMock;
    private MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.objectMapperMock = new ObjectMapper();
        this.bookControllerMock = new BookController(bookServiceMock, objectMapperMock);

        this.mockMvc= MockMvcBuilders.standaloneSetup(bookControllerMock).build();
    }

    @Test
    public void testAddNewBookIntoSystemShouldReturnStatusCreated() throws Exception {
        Book book = new Book(TITLE, AUTHOR, ISBN);
        when(bookServiceMock.addBook(book)).thenReturn("");

        mockMvc.perform(post("/api/books")
            .content(json).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated());

        verify(bookServiceMock, atLeastOnce()).addBook(any(Book.class));
    }

    @Test
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        when(bookServiceMock.getBookById(anyLong())).thenReturn(book);

        mockMvc.perform(get("/api/books/{bookID}", 1L))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        verify(bookServiceMock, atLeastOnce()).getBookById(anyLong());
    }
}
