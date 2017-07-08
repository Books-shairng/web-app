package com.ninjabooks.dao.db;

import com.ninjabooks.domain.BaseEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This helper class contains all necessary methods to deal with updates and deletes in db.
 *
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
@Transactional
class DBDaoHelper<E extends BaseEntity>
{
    private Session currentSession;
    private Transaction transaction;

    public DBDaoHelper() {
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
        this.transaction = currentSession.getTransaction();
    }

    public void update(E entity) {
       transaction.begin();
       currentSession.update(entity);
       transaction.commit();
       currentSession.close();
    }

    public void delete(E entity) {
        transaction.begin();
        currentSession.delete(entity);
        transaction.commit();
        currentSession.close();
    }

}
