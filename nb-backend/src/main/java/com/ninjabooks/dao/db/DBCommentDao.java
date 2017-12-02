package com.ninjabooks.dao.db;

import com.ninjabooks.dao.CommentDao;
import com.ninjabooks.domain.Comment;

import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DBCommentDao implements CommentDao
{
    private final SessionFactory sessionFactory;

    @Autowired
    public DBCommentDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Comment> getById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Comment comment = currentSession.get(Comment.class, id);
        return Optional.ofNullable(comment);
    }

    @Override
    public Stream<Comment> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT c FROM com.ninjabooks.domain.Comment c", Comment.class).stream();
    }

    @Override
    public void add(Comment comment) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(comment);
    }

    @Override
    public void update(Comment comment) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(comment);
    }

    @Override
    public void delete(Comment comment) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(comment);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
