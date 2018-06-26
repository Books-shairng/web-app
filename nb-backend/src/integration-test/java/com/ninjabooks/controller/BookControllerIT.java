package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;
import com.ninjabooks.util.tests.MockMvcHttpMethod;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK_STATUS;
import static com.ninjabooks.util.constants.DomainTestConstants.DESCRIPTION;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookControllerIT extends BaseITController
{
    private static final Integer EXPECTED_SIZE = 1;
    private static final String INVALID_ISBN = "978-0851310415";
    private static final int MAX_DESCRIPTION_LENGTH = 5000;
    private static final String JSON =
        "{\"title\":\"" + TITLE + "\",\"author\":\"" + AUTHOR + "\",\"isbn\":\"" + ISBN + "\"," +
        "\"description\":\"" + DESCRIPTION + "\"}";
    private static final String BASE_URL = "/api/book/";

    @Test
    public void testAddNewBookShouldReturnStatusCreated() throws Exception {
        doPost(new HttpRequestBuilder(BASE_URL)
            .withContent(JSON)
            .withStatus(CREATED)
            .build());
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
        MockMvc mvc = ((MockMvcHttpMethod) httpMethod).getMvc();
        mvc.perform(post(BASE_URL)
            .content(JSON).contentType(APPLICATION_JSON_UTF8))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.generatedCode").isNotEmpty());
    }

    @Test
    @Sql(scripts = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetDetailsBookInfoShouldReturnStatusOk() throws Exception {
        doGet(new HttpRequestBuilder(BASE_URL + "{bookID}")
            .withUrlVars(ID)
            .build());
    }

    @Test
    @Sql(scripts = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetDetailsBookInfoShouldReturnExpectedMessage() throws Exception {
        Map<String, Object> expectedJson = ImmutableMap.<String, Object>builder()
            .put("$.id", ID.intValue())
            .put("$.title", TITLE)
            .put("$.isbn", ISBN)
            .put("$.description", DESCRIPTION)
            .put("$.status", BOOK_STATUS.toString())
            .put("$.queueSize", EXPECTED_SIZE)
            .build();

        doGet(new HttpRequestBuilder(BASE_URL + "{bookID}")
            .withUrlVars(ID)
            .build(), expectedJson);
    }

    @Test
    public void testGetDetailsBookInfoShouldFailWhenBookNotFound() throws Exception {
        doGet(new HttpRequestBuilder(BASE_URL + "{bookID}")
            .withUrlVars(ID)
            .withStatus(BAD_REQUEST)
            .build());
    }

    private void addBookWithExpectedMessageAsResponse(String json, String message) throws Exception {
        doPost(new HttpRequestBuilder(BASE_URL)
            .withContent(json)
            .withStatus(BAD_REQUEST)
            .build(), singletonMap("$.message", message));
    }
}
