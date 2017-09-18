package com.ninjabooks.dao.db;

import com.ninjabooks.dao.CommentDao;
import com.ninjabooks.domain.Comment;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBCommentDao implements CommentDao
{
    private final SessionFactory sessionFactory;
    private final DBDaoHelper<Comment> daoHelper;

    @Autowired
    public DBCommentDao(SessionFactory sessionFactory, DBDaoHelper<Comment> daoHelper) {
        this.sessionFactory = sessionFactory;
        this.daoHelper = daoHelper;
    }

    @Override
    public Optional<Comment> getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        Comment comment = currentSession.get(Comment.class, id);
        return Optional.ofNullable(comment);
    }

    @Override
    public Stream<Comment> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT c FROM com.ninjabooks.domain.Comment c", Comment.class).stream();
    }

    @Override
    public void add(Comment comment) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(comment);
        currentSession.close();
    }

    @Override
    public void update(Comment comment) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.update(comment);
    }

    @Override
    public void delete(Comment comment) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.delete(comment);
    }


    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }
}
