package com.ninjabooks.service.rest.borrow.extend;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Optional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class ExtendRentalServiceImpl implements ExtendRentalService
{
    private static final Logger logger = LogManager.getLogger(ExtendRentalServiceImpl.class);

    private final BookDaoService bookDaoService;

    @Autowired
    public ExtendRentalServiceImpl(BookDaoService bookDaoService) {
        this.bookDaoService = bookDaoService;
    }

    @Override
    public void extendReturnDate(Long userID, Long bookID) throws BorrowException {
        logger.info("User with id: {} wants extend return date by two weeks", userID);

        Book book = EntityUtils.getEnity(bookDaoService, bookID);
        Borrow borrow = getBookBorrow(book);
        if (isBorrowedByUser(userID, borrow) && canExtend(borrow)) {
            borrow.setCanExtendBorrow(false);
            borrow.extendReturnDate();
        }
        else {
            final String message = MessageFormat.format("Unable extend book with id: {0}", bookID);
            throw new BorrowException(message);
        }

        logger.info("User with id: {} successfully extended return date by two weeks", userID);
    }

    private Borrow getBookBorrow(Book book) throws BorrowException {
        final String message = MessageFormat.format("Book: {0} is not borrowed", book.getTitle());
        return Optional.ofNullable(book.getBorrow()).orElseThrow(() -> new BorrowException(message));
    }

    private boolean isBorrowedByUser(Long userID, Borrow borrow) {
        User user = borrow.getUser();
        return user.getId().equals(userID);
    }

    private boolean canExtend(Borrow borrow) {
        return borrow.getCanExtendBorrow();
    }
}
