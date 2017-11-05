package com.ninjabooks.controller;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class BookControllerIT
{
    private static final int EXPECTED_SIZE = 1;
    private static final String BOOK_STATUS = DomainTestConstants.BOOK_STATUS.toString();
    private static final String ID = String.valueOf(DomainTestConstants.ID);
    private static final String JSON =
        "{" +
            "\"title\":\"" + DomainTestConstants.TITLE + "\"," +
            "\"author\":\"" + DomainTestConstants.AUTHOR + "\"," +
            "\"isbn\":\"" + DomainTestConstants.ISBN + "\"" +
        "}";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testAddNewBookShouldReturnStatusCreated() throws Exception {
        mockMvc.perform(post("/api/books/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated());
    }

    @Test
    public void testAddNewBookShouldReturnNotEmptyQRCodeMessage() throws Exception {
        mockMvc.perform(post("/api/books/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.generatedCode").isNotEmpty());
    }

    @Test
    @Sql(scripts = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/api/books/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetDetailsBookInfoShouldReturnExpectedMessage() throws Exception {
        mockMvc.perform(get("/api/books/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.author").value(DomainTestConstants.AUTHOR))
            .andExpect(jsonPath("$.title").value(DomainTestConstants.TITLE))
            .andExpect(jsonPath("$.isbn").value(DomainTestConstants.ISBN))
            .andExpect(jsonPath("$.description").value(DomainTestConstants.DESCRIPTION))
            .andExpect(jsonPath("$.status").value(BOOK_STATUS))
            .andExpect(jsonPath("$.queueSize").value(EXPECTED_SIZE));
    }

    @Test
    public void testGetDetailsBookInfoShouldFailWhenBookNotFound() throws Exception {
        mockMvc.perform(get("/api/books/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }
}
