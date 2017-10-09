package com.ninjabooks.dao.db;

import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.db.SpecifiedElementFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class DBQRCodeDao implements QRCodeDao
{
    private enum DBColumnName {DATA}

    private final SessionFactory sessionFactory;
    private final DBDaoHelper<QRCode> daoHelper;
    private final SpecifiedElementFinder specifiedElementFinder;

    @Autowired
    public DBQRCodeDao(SessionFactory sessionFactory, DBDaoHelper<QRCode> daoHelper,
                       @Qualifier(value = "queryFinder") SpecifiedElementFinder specifiedElementFinder) {
        this.sessionFactory = sessionFactory;
        this.daoHelper = daoHelper;
        this.specifiedElementFinder = specifiedElementFinder;
    }

    @Override
    public Stream<QRCode> getAll() {
        Session currentSession = sessionFactory.openSession();
        return currentSession.createQuery("SELECT q FROM com.ninjabooks.domain.QRCode q", QRCode.class).stream();
    }

    @Override
    public Optional<QRCode> getById(Long id) {
        Session currentSession = sessionFactory.openSession();
        QRCode qrCode = currentSession.get(QRCode.class, id);
        return Optional.ofNullable(qrCode);
    }

    @Override
    public Optional<QRCode> getByData(String data) {
        return specifiedElementFinder.findSpecifiedElementInDB(data, DBColumnName.DATA, QRCode.class);
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
        daoHelper.setCurrentSession(currentSession);
        daoHelper.update(qrCode);
    }


    @Override
    public void delete(QRCode qrCode) {
        Session currentSession = sessionFactory.openSession();
        daoHelper.setCurrentSession(currentSession);
        daoHelper.delete(qrCode);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.openSession();
    }

}
