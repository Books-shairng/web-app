package com.ninjabooks.controller;

import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.json.comment.CommentResponseFactory;
import com.ninjabooks.service.rest.comment.CommentRestService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentControllerTest
{
    private static final String MESSAGE_NO_COMMENTS = "Book does not contains any comments";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private CommentRestService commentRestServiceMock;

    private MockMvc mockMvc;
    private CommentController sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new CommentController(commentRestServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut).build();
    }

    @Test
    public void testFetchCommentsShouldReturnStatusOK() throws Exception {
        Set<CommentResponse> commentResponses = prepareCommentResponse();
        when(commentRestServiceMock.getComments(DomainTestConstants.ISBN)).thenReturn(commentResponses);

        mockMvc.perform(get("/api/comment/")
            .param("isbn", DomainTestConstants.ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        verify(commentRestServiceMock, atLeastOnce()).getComments(any());
    }

    @Test
    public void testFetchCommentsShouldReturnMessageWhenCommentsNotFound() throws Exception {
        mockMvc.perform(get("/api/comment/")
            .param("isbn", DomainTestConstants.ISBN))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(MESSAGE_NO_COMMENTS));
    }

    private Set<CommentResponse> prepareCommentResponse() {
        return Stream.of(CommentResponseFactory.makeCommentResponse(DomainTestConstants.COMMENT_FULL))
        .collect(Collectors.toSet());
    }
}
