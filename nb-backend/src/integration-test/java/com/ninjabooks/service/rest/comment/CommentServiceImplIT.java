package com.ninjabooks.service.rest.comment;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:it_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CommentServiceImplIT
{
    private static final String TRUNCATE_COMMENT = "TRUNCATE TABLE COMMENT ;";

    @Autowired
    private CommentService sut;

    @Test
    public void testFetchBookCommentsShouldReturnExpectedValues() throws Exception {
        Set<CommentResponse> actual = sut.getComments(DomainTestConstants.ISBN);

        assertThat(actual)
            .extracting("author", "date", "content", "isbn")
            .containsExactly(
                tuple(
                    DomainTestConstants.NAME, DomainTestConstants.COMMENT_DATE, DomainTestConstants.COMMENT_CONTENT,
                    DomainTestConstants.ISBN));
    }

    @Test
    @Sql(
        value = "classpath:it_import.sql",
        statements = TRUNCATE_COMMENT,
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFetchBookCommentsShouldReturnEmptyStreamWhenCommentsNotFound() throws Exception {
        Set<CommentResponse> actual = sut.getComments(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
    }
}
