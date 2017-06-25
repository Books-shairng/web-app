package com.ninjabooks.dao.db;

import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional
public class DBQRCodeDao implements QRCodeDao, SpecifiedElementFinder
{
    private final static Logger logger = LogManager.getLogger(DBQRCodeDao.class);

    private enum DBColumnName {DATA}

    private final SessionFactory sessionFactory;

    @Autowired
    public DBQRCodeDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;}

    @Override
    public Stream<QRCode> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.QRCode q", QRCode.class).stream();
    }

    @Override
    public QRCode getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        return currentSession.get(QRCode.class, id);
    }

    @Override
    public QRCode getByData(String data) {
        return findSpecifiedElementInDB(data, DBColumnName.DATA);
    }

    @Override
    public void add(QRCode qrCode) {
        Session currentSession = sessionFactory.openSession();
        currentSession.save(qrCode);
        currentSession.close();
    }

    @Override
    public void update(QRCode qrCode) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.update(qrCode);
        currentSession.getTransaction().commit();
        currentSession.close();
    }

    @Override
    public void delete(QRCode qrCode) {
        Session currentSession = sessionFactory.openSession();
        currentSession.getTransaction().begin();
        currentSession.delete(qrCode);
        currentSession.getTransaction().commit();
        currentSession.close();
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }

    @Override
    @SuppressWarnings("unchecked t cast")
    public <T, E> T findSpecifiedElementInDB(E parameter, Enum columnName) {
        Session currentSession = sessionFactory.openSession();
        String query = "select qr_code from com.ninjabooks.domain.QRCode qr_code where " + columnName + "=:parameter";
        Query<QRCode> qrCodeQuery = currentSession.createQuery(query, QRCode.class);
        qrCodeQuery.setParameter("parameter", parameter);

        List<QRCode> results = qrCodeQuery.getResultList();
        if (!results.isEmpty()) {
            return (T) results.get(0);
        }
        return null;
    }

}
