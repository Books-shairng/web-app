package com.ninjabooks.controller;

import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.error.handler.BorrowControllerHandler;
import com.ninjabooks.service.rest.borrow.extend.ExtendRentalService;
import com.ninjabooks.service.rest.borrow.rent.BookRentalService;
import com.ninjabooks.service.rest.borrow.returnb.BookReturnService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.ID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowControllerTest extends BaseUTController
{
    private static final String URL = "/api/borrow/";
    private static final String QR_CODE = "qrCode";
    private static final String BOOK_ID = "bookID";

    @Mock
    private BookReturnService bookReturnServiceMock;

    @Mock
    private BookRentalService bookRentalServiceMock;

    @Mock
    private ExtendRentalService extendRentalServiceMock;

    private BorrowController sut;

    @Before
    public void setUp() throws Exception {
        sut = new BorrowController(bookRentalServiceMock, bookReturnServiceMock, extendRentalServiceMock);
        mockMvc(standaloneSetup(sut).setControllerAdvice(new BorrowControllerHandler()));
    }

    @Test
    public void testBorrowBookShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameter(QR_CODE, ID.toString())
            .build());
    }


    @Test
    public void testBorrowBookShouldReturnBadRequestWhenQRCodeNotFound() throws Exception {
        doThrow(QRCodeException.class).when(bookRentalServiceMock).rentBook(anyLong(), anyString());

        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameter(QR_CODE, ID.toString())
            .withStatus(BAD_REQUEST)
            .build());

        verify(bookRentalServiceMock, atLeastOnce()).rentBook(anyLong(), anyString());
    }

    @Test
    public void testBorrowBookShouldReturnBadRequestWhenUnableToBorrow() throws Exception {
        doThrow(BorrowException.class).when(bookRentalServiceMock).rentBook(anyLong(), anyString());

        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameter(QR_CODE, ID.toString())
            .withStatus(BAD_REQUEST)
            .build());

        verify(bookRentalServiceMock, atLeastOnce()).rentBook(anyLong(), anyString());
    }

    @Test
    public void testReturnBookShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(URL + "return/")
            .withParameter(QR_CODE, ID.toString())
            .build());
    }

    @Test
    public void testReturnBookShouldFailedWhenBookIsNotBorrowed() throws Exception {
        doThrow(BorrowException.class).when(bookReturnServiceMock).returnBook(anyString());

        doPost(new HttpRequestBuilder(URL + "return/")
            .withParameter(QR_CODE, ID.toString())
            .withStatus(BAD_REQUEST)
            .build());

        verify(bookReturnServiceMock, atLeastOnce()).returnBook(anyString());
    }

    @Test
    public void testExtendReturnDateShouldSucced() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/extend/")
            .withUrlVars(ID)
            .withParameter(BOOK_ID, ID.toString())
            .build());
    }

    @Test
    public void testExtendReturnDateShouldFailedWhenUnableToExtendDate() throws Exception {
        doThrow(BorrowException.class).when(extendRentalServiceMock).extendReturnDate(any(), any());

        doPost(new HttpRequestBuilder(URL + "{userID}/extend/")
            .withUrlVars(ID)
            .withParameter(BOOK_ID, ID.toString())
            .withStatus(BAD_REQUEST)
            .build());

        verify(extendRentalServiceMock, atLeastOnce()).extendReturnDate(any(), any());
    }
}
