package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class OrderControllerIT extends AbstractBaseIT
{
    private static final String ID = String.valueOf(DomainTestConstants.ID);
    private static final String INSERT_USER = "INSERT INTO USER (id, NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) VALUES " +
        "(1, 'John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)";
    private static final String INSERT_BOOK = "INSERT INTO BOOK (id, TITLE, AUTHOR, ISBN, ACTIVE, STATUS, " +
        "DESCRIPTION) VALUES (1, 'Effective Java', 'J. Bloch', '978-0321356680', TRUE, 'FREE', 'Some description')";
    private static final String ORDER_MESSAGE = "Book was corectly ordered";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Sql(statements = {INSERT_USER, INSERT_BOOK}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testOrderBookShouldAddBookToUserQueue() throws Exception {
        mockMvc.perform(post("/api/order/{userID}/", DomainTestConstants.ID)
            .param("bookID", ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(ORDER_MESSAGE));
    }

    @Test
    @Sql(scripts = "classpath:sql_query/it_import.sql")
    public void testOrderBookShouldReturnErrorMessageWhenUserAlreadyOrderedBook() throws Exception {
        mockMvc.perform(post("/api/order/{userID}/", DomainTestConstants.ID)
            .param("bookID", ID))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(MessageFormat.format("User: {0} has already ordered this book", DomainTestConstants.ID)));
    }

    @Test
    @Sql(
        scripts = {"classpath:sql_query/it_import.sql", "classpath:sql_query/queue_overflow_script.sql"},
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testOrderBookShouldReturnErrorMessageWhenLimitExceed() throws Exception {
        mockMvc.perform(post("/api/order/{userID}/", DomainTestConstants.ID)
            .param("bookID", ID))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(MessageFormat.format("User: {0} has exceeded the limit", DomainTestConstants.ID)));
    }
}
