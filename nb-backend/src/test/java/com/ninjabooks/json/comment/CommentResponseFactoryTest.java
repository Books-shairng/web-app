package com.ninjabooks.json.comment;

import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_CONTENT;
import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_FULL;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CommentResponseFactoryTest
{
    @Test
    public void testMakeCommentResponseShouldReturnExpectedCommentContent() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(COMMENT_FULL);

        assertThat(actual).extracting("content").containsExactly(COMMENT_CONTENT);
    }

    @Test
    public void testMakeCommentResponseShouldReturnExpectedUsername() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(COMMENT_FULL);

        assertThat(actual).extracting("author").containsExactly(NAME);
    }

    @Test
    public void testMakeCommentResponseShouldReturnExpectedDate() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(COMMENT_FULL);

        assertThat(actual).extracting("date").containsExactly(COMMENT_DATE);
    }

    @Test
    public void testMakeCommentResponseShouldReturnExpectedISBn() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(COMMENT_FULL);

        assertThat(actual).extracting("isbn").containsExactly(ISBN);
    }
}
