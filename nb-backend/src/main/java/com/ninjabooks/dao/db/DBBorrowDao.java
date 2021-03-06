package com.ninjabooks.dao.db;

import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.util.db.SpecifiedElementFinder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class DBBorrowDao implements BorrowDao
{
    private enum DBColumnName {BORROW_DATE, EXPECTED_RETURN_DATE}

    private final SessionFactory sessionFactory;
    private final SpecifiedElementFinder specifiedElementFinder;

    @Autowired
    public DBBorrowDao(SessionFactory sessionFactory,
                       @Qualifier(value = "streamFinder") SpecifiedElementFinder specifiedElementFinder) {
        this.sessionFactory = sessionFactory;
        this.specifiedElementFinder = specifiedElementFinder;
    }

    @Override
    public Stream<Borrow> getAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("SELECT b FROM com.ninjabooks.domain.Borrow b", Borrow.class).stream();
    }

    @Override
    public Optional<Borrow> getById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Borrow borrow = currentSession.get(Borrow.class, id);
        return Optional.ofNullable(borrow);
    }

    @Override
    public Stream<Borrow> getByExpectedReturnDate(LocalDate returnDate) {
        return specifiedElementFinder.findSpecifiedElementInDB(returnDate, DBColumnName.EXPECTED_RETURN_DATE, Borrow.class);
    }

    @Override
    public Stream<Borrow> getByBorrowDate(LocalDate borrowDate) {
        return specifiedElementFinder.findSpecifiedElementInDB(borrowDate, DBColumnName.BORROW_DATE, Borrow.class);
    }

    @Override
    public void add(Borrow borrow) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(borrow);
    }

    @Override
    public void update(Borrow borrow) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.update(borrow);
    }

    @Override
    public void delete(Borrow borrow) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.delete(borrow);
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}
