package com.ninjabooks.service;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.error.qrcode.QRCodeUnableToCreateException;
import com.ninjabooks.util.CodeGenerator;
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
public class BookServiceImpl implements BookService
{
    private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);

    private final int DEFAULT_NUMBER_ATTEMPT = 5;

    private final BookDao bookDao;
    private final QRCodeDao qrCodeDao;
    private final CodeGenerator codeGenerator;

    @Autowired
    public  BookServiceImpl(BookDao bookDao, QRCodeDao qrCodeDao, CodeGenerator codeGenerator) {
        this.bookDao = bookDao;
        this.qrCodeDao = qrCodeDao;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public String addBook(Book book) throws QRCodeUnableToCreateException {
        QRCode createdQrCode = createNewQRCode();
        qrCodeDao.add(createdQrCode);

        book.setQRCode(createdQrCode);

        bookDao.add(book);
        logger.info("Successfully added new book: {"+ book.getTitle() +"} in system");
        return createdQrCode.getData();
    }

    @Override
    public Book getBookById(Long id) {
        logger.info("Looking for book with id: {" + id + "}");
        return bookDao.getById(id);
    }

    /**
     * Try 5 times to create new unique qr code, if fails throws exception.
     * Probablity of generating same code is very small, but this is safeguard for every
     * eventuality.
     *
     * @return unique qr code
     * @throws QRCodeUnableToCreateException if after 5th time cannot generate new qr code
     */

    private QRCode createNewQRCode() throws QRCodeUnableToCreateException {
        String generatedCode;
        logger.info("Try generate new QR code");

        for (int i = 0; i < DEFAULT_NUMBER_ATTEMPT; i++) {
            generatedCode = codeGenerator.generateCode();
            if (!isGeneratedCodeIsUnique(generatedCode)) {
                logger.info("Successful generated QR code");
                return new QRCode(generatedCode);
            }
        }
        String errorMessage = "Unable to generate unique QR code";

        logger.error(errorMessage);
        throw new QRCodeUnableToCreateException(errorMessage);
    }

    private boolean isGeneratedCodeIsUnique(String code) {
        return qrCodeDao.getByData(code) != null;
    }
}
