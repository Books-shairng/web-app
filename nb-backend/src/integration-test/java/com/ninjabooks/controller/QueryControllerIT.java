package com.ninjabooks.controller;

import com.ninjabooks.config.AbstractBaseIT;
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
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class QueryControllerIT extends AbstractBaseIT
{
    private static final String WRONG_SQL_COMMAND = "DASDAS ADASDASDAS";
    private static final String SELECT_QUERY = "SELECT * FROM USER";
    private static final String INSERT_QUERY =
        "INSERT INTO USER (NAME, EMAIL, PASSWORD, AUTHORITY, ACTIVE) " +
        "VALUES ('John Dee', 'john.dee@exmaple.com', 'Johny!Dee123', 'USER', TRUE)";
    public static final int EXPECTED_SIZE = 1;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testExecuteShouldReturnExpectedStatus() throws Exception {
        mockMvc.perform(post("/api/query")
            .param("q", SELECT_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testExecuteWithSelectQueryShouldReturnExpectedMessage() throws Exception {
        mockMvc.perform(post("/api/query")
            .param("q", SELECT_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$..ID").value(DomainTestConstants.ID.intValue()))
            .andExpect(jsonPath("$..ACTIVE").value(true))
            .andExpect(jsonPath("$..PASSWORD").value(DomainTestConstants.PASSWORD))
            .andExpect(jsonPath("$..EMAIL").value(DomainTestConstants.EMAIL))
            .andExpect(jsonPath("$..NAME").value(DomainTestConstants.NAME));
    }

    @Test
    public void testExecuteWithSelectQueryShouldRetunResponseWithExpectedSize() throws Exception {
        mockMvc.perform(post("/api/query")
            .param("q", SELECT_QUERY))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.length()").value(EXPECTED_SIZE));
    }

    @Test
    public void testExecuteWithInsertQueryShouldReturnExpectedMessage() throws Exception {
        mockMvc.perform(post("/api/query")
            .param("q", INSERT_QUERY))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$..['EXECUTE RESULT']").value(DomainTestConstants.ID.toString()));
    }

    @Test
    public void testExecuteWithWrongSQLCommandShouldThrowsException() throws Exception {
        mockMvc.perform(post("/api/query")
            .param("q", WRONG_SQL_COMMAND))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }
}