package com.ninjabooks.service.rest.borrow.returnb;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.History;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.service.dao.history.HistoryService;
import com.ninjabooks.service.rest.borrow.RentalHelper;

import java.text.MessageFormat;
import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class BookReturnServiceImpl implements BookReturnService
{
    private static final Logger logger = LogManager.getLogger(BookReturnServiceImpl.class);

    private static final LocalDate ACTUAL_DATE = LocalDate.now();

    private final HistoryService historyService;
    private final RentalHelper rentalHelper;

    @Autowired
    public BookReturnServiceImpl(HistoryService historyService, RentalHelper rentalHelper) {
        this.historyService = historyService;
        this.rentalHelper = rentalHelper;
    }

    @Override
    public void returnBook(String qrCode) throws BorrowException {
        logger.info("User wants return his book, QR code: {}", qrCode);

        Book book = rentalHelper.findBookByQRCode(qrCode);
        performReturnBook(book);

        logger.info("User successfully return book");
    }

    private void performReturnBook(Book book) throws BorrowException {
        if (rentalHelper.isBookBorrowed(book)) {
            book.setStatus(BookStatus.FREE);
            Borrow currentBorrow = book.getBorrow();
            currentBorrow.setRealReturnDate(ACTUAL_DATE);
            currentBorrow.setIsActive(false);
            addNewHistory(currentBorrow);
        }
        else {
            String message = MessageFormat.format("Book: {0} is not borrowed, unable to return", book.getTitle());
            throw new BorrowException(message);
        }
    }

    private void addNewHistory(Borrow borrow) {
        logger.info("An attempt to add a new history into user log");

        History history = new History(ACTUAL_DATE);
        Book book = borrow.getBook();
        history.setBook(book);
        User user = borrow.getUser();
        history.setUser(user);

        historyService.add(history);

        logger.info("New histtory was added to user: {} log", user.getEmail());
    }
}
