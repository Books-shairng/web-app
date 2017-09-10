package com.ninjabooks.service.dao.generic;

import com.ninjabooks.dao.GenericDao;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public abstract class GenericServiceImpl<E, K extends Serializable> implements GenericService<E, K>
{
    private GenericDao<E, K> genericDao;

    public GenericServiceImpl(GenericDao<E, K> genericDao) {
        this.genericDao = genericDao;
    }

    @Override
    public void add(E entity) {
        genericDao.add(entity);
    }

    @Override
    public void remove(E entity) {
        genericDao.delete(entity);
    }

    @Override
    public void update(E entity) {
        genericDao.update(entity);
    }

    @Override
    public Stream<E> getAll() {
        return genericDao.getAll();
    }

    @Override
    public Optional<E> getById(K id) {
        return genericDao.getById(id);
    }

    @Override
    public Session getSession() {
        return genericDao.getCurrentSession();
    }
}
