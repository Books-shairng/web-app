package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBCommentDao;
import com.ninjabooks.domain.Comment;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.util.db.SpecifiedElementFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBCommentDaoTest
{
    private static final String UPDATED_COMMENT_CONTENT = "new Comment";
    private static final Supplier<Stream<Comment>> COMMENT_STREAM_SUPPLIER =
        CommonUtils.asSupplier(DomainTestConstants.COMMENT);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    @Mock
    private SpecifiedElementFinder specifiedElementFinderMock;

    private CommentDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBCommentDao(sessionFactoryMock);
        when(sessionFactoryMock.getCurrentSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddComment() throws Exception {
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.COMMENT);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteComment() throws Exception {
        doNothing().when(sessionMock).delete(DomainTestConstants.COMMENT);
        sut.delete(DomainTestConstants.COMMENT);

        verify(sessionMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetAllShouldReturnsAllRecords() throws Exception {
        when(queryMock.stream()).thenReturn(COMMENT_STREAM_SUPPLIER.get());
        Stream<Comment> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.COMMENT);
        verify(queryMock, atLeastOnce()).stream();
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<Comment> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.COMMENT);
        Optional<Comment> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.COMMENT);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdEntityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<Comment> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testUpdateComment() throws Exception {
        Comment beforeUpdate = createFreshEntity();
        beforeUpdate.setContent(UPDATED_COMMENT_CONTENT);

        doNothing().when(sessionMock).update(beforeUpdate);
        sut.update(beforeUpdate);

        verify(sessionMock, atLeastOnce()).update(any());
    }

    private Comment createFreshEntity() {
        return new Comment(DomainTestConstants.COMMENT_CONTENT, DomainTestConstants.COMMENT_DATE);
    }
}
