package com.ninjabooks.service.rest.borrow.rent;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.BookStatus;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.error.borrow.BorrowMaximumLimitException;
import com.ninjabooks.error.qrcode.QRCodeException;
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
            throw new BorrowMaximumLimitException(MessageFormat.format("User: {0} has exceeded the limit", userID));
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
//            rentalHelper.updateBook(book);
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



    //
    //    private static final Logger logger = LogManager.getLogger(BookRentalServiceImpl.class);
    //
    //    private final int MAXIMUM_BORROW_NUMBER = 5;
    //    private final LocalDate TODAY = LocalDate.now();
//    private final BorrowDao borrowDao;
//    private final BookDao bookDao;
//    private final QRCodeDao qrCodeDao;
//    private final UserDao userDao;
//
//    @Autowired
//    public BookRentalServiceImpl(BorrowDao borrowDao, BookDao bookDao, QRCodeDao qrCodeDao, UserDao userDao) {
//        this.borrowDao = borrowDao;
//        this.bookDao = bookDao;
//        this.qrCodeDao = qrCodeDao;
//        this.userDao = userDao;
//    }
//
//    @Override
//    public void borrowBook(Long userID, Book book) throws BorrowException {
//        logger.info("User:{" + userID + "} want borrow book:{" + book.getTitle() + "}");
//        checkMaximumLimit(userID);
//
//        User currentUser = userDao.getById(userID).get();
//        Borrow borrow = new Borrow();
//        borrow.setBook(book);
//        borrow.setUser(currentUser);
//        borrow.setCanExtendBorrow(true);
//
//        logger.info("User:{" + userID + "} has successfully borrowed the book:{" + book.getTitle() + "}");
//        borrowDao.add(borrow);
//    }
//
//    @Override
//    public void borrowBook(Long userID, QRCode qrCode) throws BorrowException, QRCodeNotFoundException {
//        logger.info("Mobile client is trying to borrow a book with QR code:{" + qrCode.getData() + "}");
//        checkMaximumLimit(userID);
//
//        Book book = getBookByQRCode(qrCode);
//
//        checkQRCodeCorrectness(qrCode);
//
//        User currentUser = userDao.getById(userID).get();
//
//        Borrow borrow = new Borrow();
//        borrow.setUser(currentUser);
//        borrow.setBook(book);
//        borrow.setCanExtendBorrow(true);
//        borrow.setBorrowDate(TODAY);
//
//        logger.info("User:{" + userID + "} has successfully borrowed  book:{" + book.getTitle() + "}");
//        borrowDao.add(borrow);
//    }
//
//    @Override
//    public void extendReturnDate(Long userID, Book book) throws BorrowException {
//        logger.info("User:{" + userID + "} want extend return date of this book:{" + book.getTitle() + "}");
//        Borrow borrow = bookDao.getById(book.getId()).get().getBorrow();
//
//        checkExtendStatus(borrow);
//        borrow.extendReturnDate();
//        borrow.setCanExtendBorrow(false);
//
//        borrowDao.update(borrow);
//
//        logger.info("User:{" + userID + "} has successfully  extend return date of this book:{" + book.getTitle() + "}");
//    }
//
//    @Override
//    public void extendReturnDate(QRCode qrCode) throws BorrowException, QRCodeNotFoundException {
//        logger.info("Mobile client is trying to extend return date with QR code:{" + qrCode.getData() + "}");
//
//        checkQRCodeCorrectness(qrCode);
//        Borrow borrow = getBorrowByQRCode(qrCode);
//
//        checkExtendStatus(borrow);
//        borrow.extendReturnDate();
//        borrow.setCanExtendBorrow(false);
//
//        borrowDao.update(borrow);
//
//        logger.info("Mobile client has successfully extend the book with QR code:{" + qrCode.getData() + "}");
//    }
//
//    @Override
//    public void returnBook(QRCode qrCode) throws QRCodeNotFoundException {
//        checkQRCodeCorrectness(qrCode);
//
//        Borrow borrow = getBorrowByQRCode(qrCode);
//        borrow.setRealReturnDate(TODAY);
//        borrowDao.update(borrow);
//    }
//
//    @Override
//    public void confirmBorrow(QRCode qrCode) throws QRCodeNotFoundException {
//        checkQRCodeCorrectness(qrCode);
//
//        Borrow borrow = getBorrowByQRCode(qrCode);
//
//        borrow.setBorrowDate(TODAY);
//
//        borrowDao.update(borrow);
//    }
//
//    private Borrow getBorrowByQRCode(QRCode qrCode) {
//        Session currentSession = borrowDao.getCurrentSession();
//
//        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
//        CriteriaQuery<Borrow> criteriaQuery = builder.createQuery(Borrow.class);
//        Root<Borrow> root = criteriaQuery.from(Borrow.class);
//        Join<Borrow, Book> bookJoin = root.join("book");
//        Join<Book, QRCode> qrCodeJoin = bookJoin.join("QRCode");
//
//        criteriaQuery.select(root)
//            .where(builder.equal(qrCodeJoin.get("data"), qrCode.getData()));
//
//        return currentSession.createQuery(criteriaQuery).getSingleResult();
//    }
//

//
//    private void checkQRCodeCorrectness(QRCode qrCode) throws QRCodeNotFoundException {
//        String errorMessage;
//        if (isScannedQrCodeIsCorrect(qrCode)) {
//            errorMessage = "QR Code:{" + qrCode.getData() + "} is not recognized";
//            logger.error(errorMessage);
//            throw new QRCodeNotFoundException(errorMessage);
//        }
//    }
//
//    private void checkExtendStatus(Borrow borrow) throws BorrowException {
//        String errorMessage;
//        if (!borrow.getCanExtendBorrow()) {
//            errorMessage = "Cannot extend borrow period";
//            logger.error(errorMessage);
//            throw new BorrowException(errorMessage);
//        }
//    }
//
//    private void checkMaximumLimit(Long userID) throws BorrowMaximumLimitException {
//        User currentUser = userDao.getById(userID).get();
//        int borrows = currentUser.getBorrows().size();
//
//        if (borrows > MAXIMUM_BORROW_NUMBER) {
//            String errorMessage = "User:{" + userID + "} reach maximum borrow size";
//            logger.error(errorMessage);
//            throw new BorrowMaximumLimitException(errorMessage);
//        }
//    }
//
//
//    private boolean isScannedQrCodeIsCorrect(QRCode qrCode) {
//        return qrCodeDao.getByData(qrCode.getData()) == null;
//    }
}
