package com.ninjabooks.controller;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;
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

import java.text.MessageFormat;

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
public class BorrowControllerIT
{
//    private static final String JSON =
//        "{" +
//            "\"qrCode\" : \"" + DomainTestConstants.DATA + "\"" +
//        "}";

    private static final String UPDATE_BOOK_STATUS = "UPDATE BOOK SET STATUS = 'FREE' WHERE ID = 1;";
    private static final String RANDOM_QR_CODE = "dasdajsda";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    @Sql(value = "classpath:rent-scripts/return-script/it_return_import.sql",
        statements = UPDATE_BOOK_STATUS,
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testBorrowBookShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/borrow/{userID}/", DomainTestConstants.ID)
            .param("qrCode", DomainTestConstants.DATA))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testBorrowBookShouldReturnBadRequestWhenUserNotFound() throws Exception {
        mockMvc.perform(post("/api/borrow/{userID}/", DomainTestConstants.ID)
            .param("qrCode", DomainTestConstants.DATA))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(MessageFormat.format("Entity with id: {0} not found", DomainTestConstants.ID)));
    }

    @Test
    @Sql(value = "classpath:rent-scripts/return-script/it_return_import.sql",
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testBorrowBookShouldReturnBadRequestWhenQRCodeNotFound() throws Exception {
        mockMvc.perform(post("/api/borrow/{userID}/", DomainTestConstants.ID)
            .param("qrCode", RANDOM_QR_CODE))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(MessageFormat.format("Book not found by given qr code: {0}", RANDOM_QR_CODE)));
    }

    @Test
    @Sql(value = "classpath:rent-scripts/return-script/it_return_import.sql",
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testBorrowBookShouldReturnBadRequestWhenBookIsNotBorrowed() throws Exception {
        mockMvc.perform(post("/api/borrow/{userID}/", DomainTestConstants.ID)
            .param("qrCode", DomainTestConstants.DATA))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(MessageFormat.format("Book: {0} is already borrowed", DomainTestConstants.TITLE)));
    }

    @Test
    @Sql(value = "classpath:rent-scripts/return-script/it_return_import.sql",
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testReturnBookShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/borrow/return/")
            .param("qrCode", DomainTestConstants.DATA))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @Sql(value = "classpath:rent-scripts/return-script/it_return_import.sql",
        statements = UPDATE_BOOK_STATUS,
        executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testReturnBookShouldFailedWhenBookIsNotBorrowed() throws Exception {
        mockMvc.perform(post("/api/borrow/return/")
            .param("qrCode", DomainTestConstants.DATA))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message")
                .value(MessageFormat.format("Book: {0} is not borrowed, unable to return", DomainTestConstants.TITLE)));
    }
}
