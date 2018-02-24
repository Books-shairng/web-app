package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.DESCRIPTION;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import com.jayway.jsonpath.JsonPath;
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
public class BookControllerIT extends AbstractBaseIT
{
    private static final int EXPECTED_SIZE = 1;
    private static final String BOOK_STATUS = DomainTestConstants.BOOK_STATUS.toString();
    private static final String ID = String.valueOf(DomainTestConstants.ID);
    private static final String INVALID_ISBN = "978-0851310415";
    private static final int MAX_DESCRIPTION_LENGTH = 5000;
    private static final String JSON =
        "{" +
            "\"title\":\"" + TITLE + "\"," +
            "\"author\":\"" + AUTHOR + "\"," +
            "\"isbn\":\"" + ISBN + "\"," +
            "\"description\":\"" + DESCRIPTION + "\"" +
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
        mockMvc.perform(post("/api/book/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated());
    }

    @Test
    public void testAddNewBookWithoutTitleFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.title").jsonString();
        addBookWithExpectedMessageAsResponse(json, "title field must be not empty");
    }

    @Test
    public void testAddNewBookWithoutAuthorFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.author").jsonString();
        addBookWithExpectedMessageAsResponse(json, "author field must be not empty");
    }

    @Test
    public void testAddNewBookWithoutISBNFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.isbn").jsonString();
        addBookWithExpectedMessageAsResponse(json, "isbn field must be not empty");
    }

    @Test
    public void testAddNewBookWithoutDescriptionFieldShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).delete("$.description").jsonString();
        addBookWithExpectedMessageAsResponse(json, "description field must be not empty");
    }

    @Test
    public void testAddNewBookWithInvalidISBNShouldFailed() throws Exception {
        String json = JsonPath.parse(JSON).set("$.isbn", INVALID_ISBN).jsonString();
        addBookWithExpectedMessageAsResponse(json, "invalid ISBN");
    }

    @Test
    public void testAddNewBookWithTooLongDescriptionShouldFailed() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= MAX_DESCRIPTION_LENGTH; i++) {
            builder.append(" ");
        }
        String json = JsonPath.parse(JSON).set("$.description", builder.toString()).jsonString();
        addBookWithExpectedMessageAsResponse(json, "description is too long, maximum description size: 5000");
    }

    @Test
    public void testAddNewBookShouldReturnNotEmptyQRCodeMessage() throws Exception {
        mockMvc.perform(post("/api/book/")
            .content(JSON).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.generatedCode").isNotEmpty());
    }

    @Test
    @Sql(scripts = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        mockMvc.perform(get("/api/book/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetDetailsBookInfoShouldReturnExpectedMessage() throws Exception {
        mockMvc.perform(get("/api/book/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(jsonPath("$.id").value(ID))
            .andExpect(jsonPath("$.author").value(AUTHOR))
            .andExpect(jsonPath("$.title").value(TITLE))
            .andExpect(jsonPath("$.isbn").value(ISBN))
            .andExpect(jsonPath("$.description").value(DESCRIPTION))
            .andExpect(jsonPath("$.status").value(BOOK_STATUS))
            .andExpect(jsonPath("$.queueSize").value(EXPECTED_SIZE));
    }

    @Test
    public void testGetDetailsBookInfoShouldFailWhenBookNotFound() throws Exception {
        mockMvc.perform(get("/api/book/{bookID}", DomainTestConstants.ID))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    private void addBookWithExpectedMessageAsResponse(String json, String message) throws Exception {
        mockMvc.perform(post("/api/book/")
            .content(json).contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(message));
    }
}
