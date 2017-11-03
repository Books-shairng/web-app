package com.ninjabooks.json.comment;

import com.ninjabooks.util.constants.DomainTestConstants;
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
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(DomainTestConstants.COMMENT_FULL);

        assertThat(actual).extracting("content").containsExactly(DomainTestConstants.COMMENT_CONTENT);
    }

    @Test
    public void testMakeCommentResponseShouldReturnExpectedUsername() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(DomainTestConstants.COMMENT_FULL);

        assertThat(actual).extracting("author").containsExactly(DomainTestConstants.NAME);
    }

    @Test
    public void testMakeCommentResponseShouldReturnExpectedDate() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(DomainTestConstants.COMMENT_FULL);

        assertThat(actual).extracting("date").containsExactly(DomainTestConstants.COMMENT_DATE);
    }

    @Test
    public void testMakeCommentResponseShouldReturnExpectedISBn() throws Exception {
        CommentResponse actual = CommentResponseFactory.makeCommentResponse(DomainTestConstants.COMMENT_FULL);

        assertThat(actual).extracting("isbn").containsExactly(DomainTestConstants.ISBN);
    }
}
