package com.ninjabooks.service.rest.comment;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.Comment;
import com.ninjabooks.domain.History;
import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.service.dao.comment.CommentDaoService;
import com.ninjabooks.service.dao.history.HistoryService;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:it_import.sql", executionPhase = BEFORE_TEST_METHOD)
public class CommentRestServiceImplIT
{
    private static final String TRUNCATE_COMMENT = "TRUNCATE TABLE COMMENT ;";
    private static final String UPDATE_COMMENTED_STATUS = "UPDATE HISTORY SET COMMENTED = TRUE WHERE ID = 1 ;";
    private static final String UPDATE_RETURN_DATE =
        "UPDATE HISTORY SET RETURN_DATE = DATE_ADD(now(), INTERVAL 1 MONTH) WHERE ID = 1 ;";
    private static final String FLUSH_HISTORY_TABLE = "TRUNCATE TABLE HISTORY ;";

    @Autowired
    private CommentDaoService commentDaoService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private CommentRestService sut;

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
        executionPhase = BEFORE_TEST_METHOD)
    public void testFetchBookCommentsShouldReturnEmptyStreamWhenCommentsNotFound() throws Exception {
        Set<CommentResponse> actual = sut.getComments(DomainTestConstants.ISBN);

        assertThat(actual).isEmpty();
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndChangeCommentedToTrue() throws Exception {
        sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID);
        Stream<History> actual = historyService.getAll();

        assertThat(actual).extracting("isCommented").contains(DomainTestConstants.IS_COMMENTED);
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndReturnExpectedCommentEntity() throws Exception {
        sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID);
        Stream<Comment> actual = commentDaoService.getAll();

        assertThat(actual)
            .extracting("content", "user.name", "book.isbn")
            .contains(tuple(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.NAME, DomainTestConstants.ISBN));
    }

    @Test
    @Transactional
    @Sql(
        scripts = "classpath:comment-scripts/it_comment_script.sql",
        statements = UPDATE_COMMENTED_STATUS,
        executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldThrowsExceptionWhenBookWasAlreadyCommented() throws Exception {
        assertThatExceptionOfType(CommentException.class)
            .isThrownBy(() ->
                sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessage("Unable to add new comment");
    }

    @Test
    @Transactional
    @Sql(
        scripts = "classpath:comment-scripts/it_comment_script.sql",
        statements = UPDATE_RETURN_DATE,
        executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldThrowsExceptionWhenDateIsOverdue() throws Exception {
        assertThatExceptionOfType(CommentException.class)
            .isThrownBy(() ->
                sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessage("Unable to add new comment");
    }

    @Test
    @Transactional
    @Sql(
        scripts = "classpath:comment-scripts/it_comment_script.sql",
        statements = FLUSH_HISTORY_TABLE,
        executionPhase = BEFORE_TEST_METHOD)
    public void testAddChommentShouldThrowsExceptionHistoryEntityNotFound() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() ->
                sut.addComment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.ID, DomainTestConstants.ID))
            .withNoCause()
            .withMessageContaining("enity not found");
    }
}
