package com.ninjabooks.service;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.dao.BorrowDao;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.Borrow;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.borrow.BorrowException;
import com.ninjabooks.error.borrow.BorrowMaximumLimitException;
import com.ninjabooks.error.qrcode.QRCodeNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDate;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class BorrowServiceImpl implements BorrowService
{
    private static final Logger logger = LogManager.getLogger(BorrowServiceImpl.class);

    private final int MAXIMUM_BORROW_NUMBER = 5;
    private final LocalDate TODAY = LocalDate.now();

    private final BorrowDao borrowDao;
    private final BookDao bookDao;
    private final QRCodeDao qrCodeDao;
    private final UserDao userDao;

    @Autowired
    public BorrowServiceImpl(BorrowDao borrowDao, BookDao bookDao, QRCodeDao qrCodeDao, UserDao userDao) {
        this.borrowDao = borrowDao;
        this.bookDao = bookDao;
        this.qrCodeDao = qrCodeDao;
        this.userDao = userDao;
    }

    @Override
    public void borrowBook(Long userID, Book book) throws BorrowException {
        logger.info("User:{" + userID + "} want borrow book:{" + book.getTitle() + "}");
        checkMaximumLimit(userID);

        User currentUser = userDao.getById(userID);
        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setUser(currentUser);
        borrow.setCanExtendBorrow(true);

        logger.info("User:{" + userID + "} has successfully borrowed the book:{" + book.getTitle() + "}");
        borrowDao.add(borrow);
    }

    @Override
    public void borrowBook(Long userID, QRCode qrCode) throws BorrowException, QRCodeNotFoundException {
        logger.info("Mobile client is trying to borrow a book with QR code:{" + qrCode.getData() + "}");
        checkMaximumLimit(userID);

        Book book = getBookByQRCode(qrCode);

        checkQRCodeCorrectness(qrCode);

        User currentUser = userDao.getById(userID);

        Borrow borrow = new Borrow();
        borrow.setUser(currentUser);
        borrow.setBook(book);
        borrow.setIsBorrowed(true);
        borrow.setCanExtendBorrow(true);
        borrow.setBorrowDate(TODAY);

        logger.info("User:{" + userID + "} has successfully borrowed  book:{" + book.getTitle() + "}");
        borrowDao.add(borrow);
    }

    @Override
    public void extendReturnDate(Long userID, Book book) throws BorrowException {
        logger.info("User:{" + userID + "} want extend return date of this book:{" + book.getTitle() + "}");
        Borrow borrow = bookDao.getById(book.getId()).getBorrows().get(0);

        checkExtendStatus(borrow);
        borrow.extendReturnDate();
        borrow.setCanExtendBorrow(false);

        borrowDao.update(borrow);

        logger.info("User:{" + userID + "} has successfully  extend return date of this book:{" + book.getTitle() + "}");
    }

    @Override
    public void extendReturnDate(QRCode qrCode) throws BorrowException, QRCodeNotFoundException {
        logger.info("Mobile client is trying to extend return date with QR code:{" + qrCode.getData() + "}");

        checkQRCodeCorrectness(qrCode);
        Borrow borrow = getBorrowByQRCode(qrCode);

        checkExtendStatus(borrow);
        borrow.extendReturnDate();
        borrow.setCanExtendBorrow(false);

        borrowDao.update(borrow);

        logger.info("Mobile client has successfully extend the book with QR code:{" + qrCode.getData() + "}");
    }

    @Override
    public void returnBook(QRCode qrCode) throws QRCodeNotFoundException {
        checkQRCodeCorrectness(qrCode);

        Borrow borrow = getBorrowByQRCode(qrCode);
        borrow.setIsBorrowed(false);
        borrow.setReturnDate(TODAY);
        borrowDao.update(borrow);
    }

    @Override
    public void confirmBorrow(QRCode qrCode) throws QRCodeNotFoundException {
        checkQRCodeCorrectness(qrCode);

        Borrow borrow = getBorrowByQRCode(qrCode);

        borrow.setIsBorrowed(true);
        borrow.setBorrowDate(TODAY);

        borrowDao.update(borrow);
    }

    private Borrow getBorrowByQRCode(QRCode qrCode) {
        Session currentSession = borrowDao.getCurrentSession();

        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Borrow> criteriaQuery = builder.createQuery(Borrow.class);
        Root<Borrow> root = criteriaQuery.from(Borrow.class);
        Join<Borrow, Book> bookJoin = root.join("book");
        Join<Book, QRCode> qrCodeJoin = bookJoin.join("QRCode");

        criteriaQuery.select(root)
            .where(builder.equal(qrCodeJoin.get("data"), qrCode.getData()));

        return currentSession.createQuery(criteriaQuery).getSingleResult();
    }

    private Book getBookByQRCode(QRCode qrCode) {
        Session currentSession = bookDao.getCurrentSession();

        CriteriaBuilder builder = currentSession.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = builder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        Join<Book, QRCode> qrCodeJoin = root.join("QRCode");

        criteriaQuery
            .select(root)
            .where(builder.equal(qrCodeJoin.get("data"), qrCode.getData()));

        return currentSession.createQuery(criteriaQuery).getSingleResult();
    }

    private void checkQRCodeCorrectness(QRCode qrCode) throws QRCodeNotFoundException {
        String errorMessage;
        if (isScannedQrCodeIsCorrect(qrCode)) {
            errorMessage = "QR Code:{" + qrCode.getData() +"} is not recognized";
            logger.error(errorMessage);
            throw new QRCodeNotFoundException(errorMessage);
        }
    }

    private void checkExtendStatus(Borrow borrow) throws BorrowException {
        String errorMessage;
        if (!borrow.getCanExtendBorrow()) {
            errorMessage = "Cannot extend borrow period";
            logger.error(errorMessage);
            throw new BorrowException(errorMessage);
        }
    }

    private void checkMaximumLimit(Long userID) throws BorrowMaximumLimitException {
        User currentUser = userDao.getById(userID);
        int borrows = currentUser.getBorrows().size();

        if (borrows > MAXIMUM_BORROW_NUMBER) {
            String errorMessage = "User:{" + userID +"} reach maximum borrow size";
            logger.error(errorMessage);
            throw new BorrowMaximumLimitException(errorMessage);
        }
    }


    private boolean isScannedQrCodeIsCorrect(QRCode qrCode) {
        return qrCodeDao.getByData(qrCode.getData()) == null;
    }
}
