package com.ninjabooks.controller;

import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.error.handler.CommentControlllerHandler;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.comment.CommentResponseFactory;
import com.ninjabooks.service.rest.comment.CommentRestService;
import com.ninjabooks.util.tests.HttpRequest.HttpRequestBuilder;

import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_CONTENT;
import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static java.util.stream.Collectors.toSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentControllerTest extends BaseUTController
{
    private static final String URL = "/api/comment/";
    private static final String MESSAGE_NO_COMMENTS = "Book does not contains any comments";
    private static final String JSON_REQUEST_WITH_COMMENT =
        "{\"comment\":\"" + COMMENT_CONTENT + "\"}";

    @Mock
    private CommentRestService commentRestServiceMock;

    private CommentController sut;

    @Before
    public void setUp() throws Exception {
        sut = new CommentController(commentRestServiceMock);
        mockMvc(standaloneSetup(sut).setControllerAdvice(new CommentControlllerHandler()));
    }

    @Test
    public void testFetchCommentsShouldReturnStatusOK() throws Exception {
        when(commentRestServiceMock.getComments(ISBN)).thenReturn(prepareCommentResponse());

        doGet(new HttpRequestBuilder(URL)
            .withParameter("isbn", ISBN)
            .build());

        verify(commentRestServiceMock, atLeastOnce()).getComments(any());
    }

    @Test
    public void testFetchCommentsShouldReturnMessageWhenCommentsNotFound() throws Exception {
        Map<String, Object> json = ImmutableMap.of("$.message", MESSAGE_NO_COMMENTS);
        doGet(new HttpRequestBuilder(URL)
            .withParameter("isbn", ISBN)
            .build(), json);
    }


    @Test
    public void testAddCommentShouldSucceedAndReturnStatusOK() throws Exception {
        doPost(new HttpRequestBuilder(URL + "{userID}/add")
            .withUrlVars(ID)
            .withParameter("bookID", String.valueOf(ID))
            .withContent(JSON_REQUEST_WITH_COMMENT)
            .build());

        verify(commentRestServiceMock, atLeastOnce()).addComment(anyString(), anyLong(), anyLong());
    }

    @Test
    public void testAddCommentShouldFaildWhenUnableToAddComment() throws Exception {
        doThrow(CommentException.class).when(commentRestServiceMock).addComment(anyString(), anyLong(), anyLong());

        doPost(new HttpRequestBuilder(URL + "{userID}/add")
            .withUrlVars(ID)
            .withParameter("bookID", String.valueOf(ID))
            .withContent(JSON_REQUEST_WITH_COMMENT)
            .withStatus(BAD_REQUEST)
            .build());

        verify(commentRestServiceMock, atLeastOnce()).addComment(anyString(), anyLong(), anyLong());
    }

    private Set<CommentResponse> prepareCommentResponse() {
        return Stream.of(CommentResponseFactory.makeCommentResponse(COMMENT_FULL))
            .collect(toSet());
    }
}
