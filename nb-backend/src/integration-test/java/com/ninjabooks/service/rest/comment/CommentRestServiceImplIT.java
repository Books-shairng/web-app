package com.ninjabooks.service.rest.comment;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.Comment;
import com.ninjabooks.domain.History;
import com.ninjabooks.error.exception.comment.CommentException;
import com.ninjabooks.json.comment.CommentResponse;
import com.ninjabooks.service.dao.comment.CommentDaoService;
import com.ninjabooks.service.dao.history.HistoryService;

import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_CONTENT;
import static com.ninjabooks.util.constants.DomainTestConstants.COMMENT_DATE;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.IS_COMMENTED;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
public class CommentRestServiceImplIT extends AbstractBaseIT
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
        Set<CommentResponse> actual = sut.getComments(ISBN);

        assertThat(actual)
            .extracting("author", "date", "content", "isbn")
            .containsExactly(tuple(NAME, COMMENT_DATE, COMMENT_CONTENT, ISBN));
    }

    @Test
    @Sql(
        value = "classpath:sql_query/it_import.sql",
        statements = TRUNCATE_COMMENT,
        executionPhase = BEFORE_TEST_METHOD)
    public void testFetchBookCommentsShouldReturnEmptyStreamWhenCommentsNotFound() throws Exception {
        Set<CommentResponse> actual = sut.getComments(ISBN);

        assertThat(actual).isEmpty();
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:sql_query/comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndChangeCommentedToTrue() throws Exception {
        sut.addComment(COMMENT_CONTENT, ID, ID);
        Stream<History> actual = historyService.getAll();

        assertThat(actual).extracting("isCommented").contains(IS_COMMENTED);
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:sql_query/comment-scripts/it_comment_script.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldSucceedAndReturnExpectedCommentEntity() throws Exception {
        sut.addComment(COMMENT_CONTENT, ID, ID);
        Stream<Comment> actual = commentDaoService.getAll();

        assertThat(actual)
            .extracting("content", "user.name", "book.isbn")
            .contains(tuple(COMMENT_CONTENT, NAME, ISBN));
    }

    @Test
    @Transactional
    @Sql(
        scripts = "classpath:sql_query/comment-scripts/it_comment_script.sql",
        statements = UPDATE_COMMENTED_STATUS,
        executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldThrowsExceptionWhenBookWasAlreadyCommented() throws Exception {
        assertThatExceptionOfType(CommentException.class)
            .isThrownBy(() ->
                sut.addComment(COMMENT_CONTENT, ID, ID))
            .withNoCause()
            .withMessage("Unable to add new comment");
    }

    @Test
    @Transactional
    @Sql(
        scripts = "classpath:sql_query/comment-scripts/it_comment_script.sql",
        statements = UPDATE_RETURN_DATE,
        executionPhase = BEFORE_TEST_METHOD)
    public void testAddCommentShouldThrowsExceptionWhenDateIsOverdue() throws Exception {
        assertThatExceptionOfType(CommentException.class)
            .isThrownBy(() ->
                sut.addComment(COMMENT_CONTENT, ID, ID))
            .withNoCause()
            .withMessage("Unable to add new comment");
    }

    @Test
    @Transactional
    @Sql(
        scripts = "classpath:sql_query/comment-scripts/it_comment_script.sql",
        statements = FLUSH_HISTORY_TABLE,
        executionPhase = BEFORE_TEST_METHOD)
    public void testAddChommentShouldThrowsExceptionHistoryEntityNotFound() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() ->
                sut.addComment(COMMENT_CONTENT, ID, ID))
            .withNoCause()
            .withMessageContaining("enity not found");
    }
}
