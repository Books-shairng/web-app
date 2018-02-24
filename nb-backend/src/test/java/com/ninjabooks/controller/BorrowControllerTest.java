package com.ninjabooks.controller;

import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.error.handler.BorrowControllerHandler;
import com.ninjabooks.service.rest.borrow.extend.ExtendRentalService;
import com.ninjabooks.service.rest.borrow.rent.BookRentalService;
import com.ninjabooks.service.rest.borrow.returnb.BookReturnService;

import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowControllerTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private BookReturnService bookReturnServiceMock;

    @Mock
    private BookRentalService bookRentalServiceMock;

    @Mock
    private ExtendRentalService extendRentalServiceMock;

    private MockMvc mockMvc;
    private BorrowController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BorrowController(bookRentalServiceMock, bookReturnServiceMock, extendRentalServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
            .setControllerAdvice(new BorrowControllerHandler())
            .build();
    }

    @Test
    public void testBorrowBookShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/borrow/{userID}/", ID)
            .param("qrCode", DATA))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testBorrowBookShouldReturnBadRequestWhenQRCodeNotFound() throws Exception {
        doThrow(QRCodeException.class).when(bookRentalServiceMock).rentBook(anyLong(), anyString());

        mockMvc.perform(post("/api/borrow/{userID}/", ID)
            .param("qrCode", DATA))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(bookRentalServiceMock, atLeastOnce()).rentBook(anyLong(), anyString());
    }

    @Test
    public void testBorrowBookShouldReturnBadRequestWhenUnableToBorrow() throws Exception {
        doThrow(BorrowException.class).when(bookRentalServiceMock).rentBook(anyLong(), anyString());

        mockMvc.perform(post("/api/borrow/{userID}/", ID)
            .param("qrCode", DATA))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(bookRentalServiceMock, atLeastOnce()).rentBook(anyLong(), anyString());
    }

    @Test
    public void testReturnBookShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/borrow/return/")
            .param("qrCode", DATA))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testReturnBookShouldFailedWhenBookIsNotBorrowed() throws Exception {
        doThrow(BorrowException.class).when(bookReturnServiceMock).returnBook(anyString());

        mockMvc.perform(post("/api/borrow/return/")
            .param("qrCode", DATA))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(bookReturnServiceMock, atLeastOnce()).returnBook(anyString());
    }

    @Test
    public void testExtendReturnDateShouldSucced() throws Exception {
        mockMvc.perform(post("/api/borrow/{userID}/extend/", ID)
            .param("bookID", String.valueOf(ID)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testExtendReturnDateShouldFailedWhenUnableToExtendDate() throws Exception {
        doThrow(BorrowException.class).when(extendRentalServiceMock).extendReturnDate(any(), any());

        mockMvc.perform(post("/api/borrow/{userID}/extend/", ID)
            .param("bookID", String.valueOf(ID)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(extendRentalServiceMock, atLeastOnce()).extendReturnDate(any(), any());
    }
}
