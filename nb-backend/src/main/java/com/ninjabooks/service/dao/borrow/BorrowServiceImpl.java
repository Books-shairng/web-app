package com.ninjabooks.service.dao.borrow;

import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.service.dao.generic.GenericServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class BorrowServiceImpl extends GenericServiceImpl<Borrow, Long> implements BorrowService
{
    private final BorrowDao borrowDao;

    public BorrowServiceImpl(GenericDao<Borrow, Long> genericDao, BorrowDao borrowDao) {
        super(genericDao);
        this.borrowDao = borrowDao;
    }

    @Override
    public Stream<Borrow> getByBorrowDate(LocalDate borrowDate) {
        return borrowDao.getByBorrowDate(borrowDate);
    }

    @Override
    public Stream<Borrow> getByExpectedReturnDate(LocalDate returnDate) {
        return borrowDao.getByExpectedReturnDate(returnDate);
    }
}
