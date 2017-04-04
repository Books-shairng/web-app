package com.ninjabooks.dao.db;

import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBQRCodeDao implements QRCodeDao
{
    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBQRCodeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            currentSession = sessionFactory.openSession();
        }
    }

    @Override
    public Stream<QRCode> getAll() {
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.QRCode q").stream();
    }

    @Override
    public QRCode getById(Long id) {
        return currentSession.get(QRCode.class, id);
    }

    @Override
    public void add(QRCode qrCode) {
        currentSession.save(qrCode);
    }

    @Override
    public void update(Long id) {
        currentSession.update(id);
    }

    @Override
    public void delete(Long id) {
        QRCode qrCode = currentSession.get(QRCode.class, id);
        currentSession.delete(qrCode);
    }

    @Override
    public QRCode getByData(QRCode data) {
        return currentSession.get(QRCode.class, data);
    }

    @Override
    public Session getCurrentSession() {
        return currentSession;
    }
}
