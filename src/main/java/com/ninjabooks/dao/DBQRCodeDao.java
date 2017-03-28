package com.ninjabooks.dao;

import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
public class DBQRCodeDao implements GenericDao<QRCode>
{
    private final Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();

    public DBQRCodeDao() {
    }

    @Override
    public Stream<QRCode> getAll() {
        return currentSession.createQuery("SELECT q FROM QRCode q").stream();
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
        currentSession.delete(id);
    }
}
