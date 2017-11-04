package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.borrow.BorrowException;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.service.rest.borrow.RentalHelper;
import com.ninjabooks.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class BookRentalServiceImpl implements BookRentalService
{
    private static final Logger logger = LogManager.getLogger(BookRentalServiceImpl.class);

    private static final LocalDate ACTUAL_DATE = LocalDate.now();
    private static final int MAXIMUM_LIMIT = 5;

    private final BorrowService borrowService;
    private final UserService userService;
    private final RentalHelper rentalHelper;

    @Autowired
    public BookRentalServiceImpl(BorrowService borrowService, UserService userService, RentalHelper rentalHelper) {
        this.borrowService = borrowService;
        this.userService = userService;
        this.rentalHelper = rentalHelper;
    }

    @Override
    public void rentBook(Long userID, String qrCodeData) throws QRCodeException, BorrowException {
        logger.info("User with id: {} want borrow book with follow qr code: {}", userID, qrCodeData);

        User currentUser = EntityUtils.getEnity(userService, userID);
        if (isLimitExceed(currentUser)) {
            throw new BorrowException(MessageFormat.format("User: {0} has exceeded the limit", userID));
        }

        Book book = rentalHelper.findBookByQRCode(qrCodeData);
        checkBookIsAlreadyBorrowed(book);
        performRentBook(userID, currentUser, book);
    }

    private void performRentBook(Long userID, User currentUser, Book book) throws BorrowException {
        if (rentalHelper.isNotBelongToOtherUserQueue(book, currentUser)) {
            Borrow borrow = new Borrow(book, currentUser);
            borrow.setBorrowDate(ACTUAL_DATE);
            borrowService.add(borrow);

            book.setStatus(BookStatus.BORROWED);
            book.setBorrow(borrow);
            rentalHelper.updateQueue(currentUser, book);
            logger.info("User: {} has successfully borrowed book", userID);
        }
        else {
            throw new BorrowException("Unable to borrow book");
        }
    }

    private void checkBookIsAlreadyBorrowed(Book book) throws BorrowException {
        if (rentalHelper.isBookBorrowed(book)) {
            String meesage = MessageFormat.format("Book: {0} is already borrowed", book.getTitle());
            throw new BorrowException(meesage);
        }
    }

    private boolean isLimitExceed(User currentUser) {
        return currentUser.getBorrows().size() >= MAXIMUM_LIMIT;
    }

}
