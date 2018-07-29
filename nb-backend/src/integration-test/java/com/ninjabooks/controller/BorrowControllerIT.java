package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.MultiValueMap;

import static java.text.MessageFormat.format;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BorrowControllerIT extends BaseITController
{
    private static final String UPDATE_BOOK_STATUS = "UPDATE BOOK SET STATUS = 'FREE' WHERE ID = 1;";
    private static final String RANDOM_QR_CODE = RandomStringUtils.random(1);
    private static final String TRUNCATE_BOOK_TABLE = "TRUNCATE TABLE BOOK ;";
    private static final String TRUNCATE_BORROW_TABLE = "TRUNCATE TABLE BORROW ;";
    private static final String UPDATE_EXTEND_STATUS = "UPDATE BORROW SET CAN_EXTEND_RETURN_DATE=FALSE WHERE ID=1 ;";
    private static final String URL = "/api/borrow/";
    private static final MultiValueMap<String, String> QR_CODE_PARAMS =
        convertParams(singletonMap("qrCode", singletonList(DATA)));
    private static final MultiValueMap<String, String> BOOK_ID_PARAMS =
        convertParams(singletonMap("bookID", singletonList(ID.toString())));

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         statements = UPDATE_BOOK_STATUS,
         executionPhase = BEFORE_TEST_METHOD)
    public void testBorrowBookShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameters(QR_CODE_PARAMS)
            .build());
    }

    @Test
    public void testBorrowBookShouldFailedWhenUserNotFound() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameters(QR_CODE_PARAMS)
            .withStatus(BAD_REQUEST)
            .build(), generateExpectedJson("Entity with id: {0} not found", ID));
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         executionPhase = BEFORE_TEST_METHOD)
    public void testBorrowBookShouldReturnBadRequestWhenQRCodeNotFound() throws Exception {
        Map<String, List<String>> params = singletonMap("qrCode", singletonList(RANDOM_QR_CODE));

        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameters(convertParams(params))
            .withStatus(BAD_REQUEST)
            .build(),
            generateExpectedJson("Book not found by given qr code: {0}", RANDOM_QR_CODE));
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         executionPhase = BEFORE_TEST_METHOD)
    public void testBorrowBookShouldReturnBadRequestWhenBookIsNotBorrowed() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/")
            .withUrlVars(ID)
            .withParameters(QR_CODE_PARAMS)
            .withStatus(BAD_REQUEST)
            .build(), generateExpectedJson("Book: {0} is already borrowed", TITLE));

    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         executionPhase = BEFORE_TEST_METHOD)
    public void testReturnBookShouldSucceed() throws Exception {
        doPost(new HttpRequestBuilder(URL + "return/")
            .withParameters(QR_CODE_PARAMS)
            .build());
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         statements = UPDATE_BOOK_STATUS,
         executionPhase = BEFORE_TEST_METHOD)
    public void testReturnBookShouldFailedWhenBookIsNotBorrowed() throws Exception {
        Map<String, List<String>> params = QR_CODE_PARAMS;

        doPost(new HttpRequestBuilder(URL + "return/")
            .withUrlVars(ID)
            .withParameters(QR_CODE_PARAMS)
            .withStatus(BAD_REQUEST)
            .build(), generateExpectedJson("Book: {0} is not borrowed, unable to return", TITLE));
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         executionPhase = BEFORE_TEST_METHOD)
    public void testExtendReturnDateShouldSucced() throws Exception {
        Map<String, List<String>> params = singletonMap("bookID", singletonList(String.valueOf(ID)));

        doPost(new HttpRequestBuilder(URL + "{userID}/extend/")
            .withUrlVars(ID)
            .withParameters(convertParams(params))
            .build());
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         statements = {TRUNCATE_BORROW_TABLE, TRUNCATE_BOOK_TABLE},
         executionPhase = BEFORE_TEST_METHOD)
    public void testExtendBookShouldFailedWhenBookNotExist() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/extend/")
            .withUrlVars(ID)
            .withParameters(BOOK_ID_PARAMS)
            .withStatus(BAD_REQUEST)
            .build(), generateExpectedJson("Entity with id: {0} not found", ID));
    }

    @Test
    @Sql(statements = TRUNCATE_BORROW_TABLE,
         value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         executionPhase = BEFORE_TEST_METHOD)
    public void testExtendBookShouldFailedWhenBookIsNotBorrowed() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/extend/")
            .withUrlVars(ID)
            .withParameters(BOOK_ID_PARAMS)
            .withStatus(BAD_REQUEST)
            .build(), generateExpectedJson("Book: {0} is not borrowed", TITLE));
    }

    @Test
    @Sql(value = "classpath:sql_query/rent-scripts/return-script/it_return_import.sql",
         statements = UPDATE_EXTEND_STATUS,
         executionPhase = BEFORE_TEST_METHOD)
    public void testExtendBookShouldFailedWhenExtendStatusIsFalse() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/extend/")
            .withUrlVars(ID)
            .withParameters(BOOK_ID_PARAMS)
            .withStatus(BAD_REQUEST)
            .build(), generateExpectedJson("Unable extend book with id: {0}", ID));
    }

    private static Map<String, Object> generateExpectedJson(String message, Object ... contant) {
        return singletonMap("$.message", format(message, contant));
    }
}
