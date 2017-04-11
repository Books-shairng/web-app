package com.ninjabooks.dao.db;

import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBQRCodeDao implements QRCodeDao
{
    private final static Logger logger = LogManager.getLogger(DBQRCodeDao.class);

    private final SessionFactory sessionFactory;
    private Session currentSession;

    @Autowired
    public DBQRCodeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        try {
            logger.info("Try obtain current session");
            this.currentSession = sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            logger.error(e);
            logger.info("Open new session");
            this.currentSession = sessionFactory.openSession();
        }
    }

    @Override
    public Stream<QRCode> getAll() {
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.QRCode q", QRCode.class).stream();
    }

    @Override
    public QRCode getById(Long id) {
        return currentSession.get(QRCode.class, id);
    }

    @Override
    public QRCode getByData(String data) {
        Query<QRCode> qrCodeQuery =  currentSession.createQuery("SELECT q from com.ninjabooks.domain.QRCode q where q =:DATA", QRCode.class);
        qrCodeQuery.setParameter("DATA", data);
        return qrCodeQuery.getSingleResult();
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
    public Session getCurrentSession() {
        return currentSession;
    }
}
