package com.ninjabooks.dao.db;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.CommentDao;
import com.ninjabooks.domain.Comment;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBCommentDaoIT extends AbstractBaseIT
{
    private static final String UPDATED_COMMENT_CONTENT = "new Comment";

    @Autowired
    private CommentDao sut;

    @Test
    public void testAddComment() throws Exception {
        sut.add(DomainTestConstants.COMMENT);
        Stream<Comment> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.COMMENT);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteComment() throws Exception {
        sut.delete(DomainTestConstants.COMMENT);
        Stream<Comment> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        Stream<Comment> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.COMMENT);
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Comment> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<Comment> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.COMMENT);
    }

    @Test
    public void testGetByIdEntityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Comment> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }


    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateComment() throws Exception {
        Comment entityToUpdate = createFreshEnity();
        entityToUpdate.setContent(UPDATED_COMMENT_CONTENT);

        sut.update(entityToUpdate);
        Stream<Comment> actual = sut.getAll();

        Assertions.assertThat(actual).containsExactly(entityToUpdate);
    }

    private Comment createFreshEnity() {
        Comment updatedEnity = new Comment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.COMMENT_DATE);
        updatedEnity.setId(DomainTestConstants.ID);

        return updatedEnity;
    }
}
