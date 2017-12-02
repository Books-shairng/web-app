package com.ninjabooks.service.dao.comment;

import com.ninjabooks.dao.CommentDao;
import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Comment;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class CommentDaoServiceImpl extends GenericServiceImpl<Comment, Long> implements CommentDaoService
{
    private final CommentDao commentDao;

    @Autowired
    public CommentDaoServiceImpl(GenericDao<Comment, Long> genericDao, CommentDao commentDao) {
        super(genericDao);
        this.commentDao = commentDao;
    }
}
