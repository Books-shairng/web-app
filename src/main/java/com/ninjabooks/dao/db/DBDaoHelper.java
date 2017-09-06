package com.ninjabooks.dao.db;

import com.ninjabooks.domain.BaseEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * This helper class contains all necessary methods to deal with updates and deletes in db.
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Component
@Transactional
public class DBDaoHelper<E extends BaseEntity>
{
    private enum Task {UPDATE, DELETE}

    private Session currentSession;
    private Transaction transaction;

    public DBDaoHelper() {
    }

    void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
        this.transaction = currentSession.getTransaction();
    }

    public void update(E entity) {
        performTask(entity, Task.UPDATE);
    }

    public void delete(E entity) {
        performTask(entity, Task.DELETE);
    }

    private void performTask(E entity, Task task) {
        transaction.begin();
        if (task == Task.UPDATE)
            currentSession.update(entity);
        else
            currentSession.delete(entity);
        transaction.commit();
        currentSession.close();
    }
}
