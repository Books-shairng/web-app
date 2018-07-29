package com.ninjabooks.controller;

import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.CAN_EXTEND;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;
import static com.ninjabooks.utils.JSONDateConstans.BORROW_DATE;
import static com.ninjabooks.utils.JSONDateConstans.ORDER_DATE;
import static com.ninjabooks.utils.JSONDateConstans.RETURN_DATE;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class NotificationControllerIT extends BaseITController
{
    private static final String SQL_UPDATE_BORROW_QUERY = "UPDATE BORROW SET ACTIVE=false WHERE ID=1";
    private static final String SQL_UPDATE_QUEUE_QUERY = "UPDATE QUEUE SET ACTIVE=false WHERE ID=1";
    private static final String URL = "/api/notification/1";

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql")
    public void testUserNotificationShouldFoundUserBorrowsAndQueues() throws Exception {
        Map<String, Object> notification = ImmutableMap.<String, Object>builder()
            .put("$.['Queues list'][0].id", ID.intValue())
            .put("$.['Queues list'][0].author", AUTHOR)
            .put("$.['Queues list'][0].title", TITLE)
            .put("$.['Queues list'][0].isbn", ISBN)
            .put("$.['Queues list'][0].orderDate", ORDER_DATE.value())
            .put("$.['Books list'][0].id", ID.intValue())
            .put("$.['Books list'][0].author", AUTHOR)
            .put("$.['Books list'][0].title", TITLE)
            .put("$.['Books list'][0].isbn", ISBN)
            .put("$.['Books list'][0].borrowDate", BORROW_DATE.value())
            .put("$.['Books list'][0].expectedReturnDate", RETURN_DATE.value())
            .put("$.['Books list'][0].canExtendBorrow", CAN_EXTEND)
            .build();

        doGet(new HttpRequestBuilder(URL)
            .build(), notification);
        }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", statements = {SQL_UPDATE_QUEUE_QUERY})
    public void testUserNotificationShouldReutnOnlyBookList() throws Exception {
        Map<String, Object> notification = ImmutableMap.<String, Object>builder()
            .put("$.['Books list'][0].id", ID.intValue())
            .put("$.['Books list'][0].author", AUTHOR)
            .put("$.['Books list'][0].title", TITLE)
            .put("$.['Books list'][0].isbn", ISBN)
            .put("$.['Books list'][0].borrowDate", BORROW_DATE.value())
            .put("$.['Books list'][0].expectedReturnDate", RETURN_DATE.value())
            .put("$.['Books list'][0].canExtendBorrow", CAN_EXTEND)
            .build();

        doGet(new HttpRequestBuilder(URL)
            .build(), notification);
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", statements = {SQL_UPDATE_BORROW_QUERY})
    public void testUserNotificationShouldReturnOnlyQueuesList() throws Exception {
        Map<String, Object> notification = ImmutableMap.<String, Object>builder()
            .put("$.['Queues list'][0].id", ID.intValue())
            .put("$.['Queues list'][0].author", AUTHOR)
            .put("$.['Queues list'][0].title", TITLE)
            .put("$.['Queues list'][0].isbn", ISBN)
            .put("$.['Queues list'][0].orderDate", ORDER_DATE.value())
            .build();

        doGet(new HttpRequestBuilder(URL)
            .build(), notification);
    }

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", statements = {SQL_UPDATE_BORROW_QUERY, SQL_UPDATE_QUEUE_QUERY})
    public void testUserNotificationShouldReturnStatusNotContent() throws Exception {
        doGet(new HttpRequestBuilder(URL)
            .build());
    }

    @Test
    public void testUserNotificationShouldReturnStatusBadRequest() throws Exception {
        Map<String, Object> res = ImmutableMap.of(
            "$.status", BAD_REQUEST.value(),
            "$.request", "/api/notification/1",
            "$.message", "Entity with id: 1 not found"
        );
        doGet(new HttpRequestBuilder(URL)
            .withStatus(BAD_REQUEST)
            .build(), res);
    }
}
