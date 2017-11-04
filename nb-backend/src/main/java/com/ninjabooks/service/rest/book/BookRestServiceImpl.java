package com.ninjabooks.service.rest.book;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.error.qrcode.QRCodeUnableToCreateException;
import com.ninjabooks.json.book.BookInfo;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.qrcode.QRCodeService;
import com.ninjabooks.util.EntityUtils;
import com.ninjabooks.util.QRCodeGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class BookRestServiceImpl implements BookRestService
{
    private static final Logger logger = LogManager.getLogger(BookRestServiceImpl.class);
    private static final int DEFAULT_NUMBER_ATTEMPT = 5;

    private final BookDaoService bookService;
    private final QRCodeService qrCodeService;
    private final QRCodeGenerator codeGenerator;

    @Autowired
    public BookRestServiceImpl(BookDaoService bookService, QRCodeService qrCodeService, QRCodeGenerator codeGenerator) {
        this.bookService = bookService;
        this.qrCodeService = qrCodeService;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public String addBook(Book book) throws QRCodeUnableToCreateException {
        logger.info("An attempt to add new book: {} into system", book.getTitle());

        QRCode generatedQRCode = generateQRCode();
        qrCodeService.add(generatedQRCode);
        book.setQRCode(generatedQRCode);
        bookService.add(book);

        logger.info("Successfully added new book: {} in system with follow QR code",
            book.getTitle(), book.getQRCode());
        return generatedQRCode.getData();
    }

    @Override
    public BookInfo getBookInfo(Long id) {
        Book book = EntityUtils.getEnity(bookService, id);
        Hibernate.initialize(book.getQueues());

        return new BookInfo(book);
    }

    /**
     * Try 5 times to create new unique qr code, if fails throws exception.
     * Probablity of generating same code is very small, but this is safeguard for every
     * eventuality.
     *
     * @return unique qr code
     * @throws QRCodeUnableToCreateException if after 5th time cannot generate new qr code
     */
    private QRCode generateQRCode() throws QRCodeUnableToCreateException {
        logger.info("Try generate new QR code");

        String generatedCode = null;
        for (int i = 0; i < DEFAULT_NUMBER_ATTEMPT; i++) {
            generatedCode = codeGenerator.generateCode();
            if (isGeneratedCodeIsNotUnique(generatedCode)) {
                throw new QRCodeUnableToCreateException("Unable to generate unique QR code");
            }
        }

        logger.info("Successfully generated QR code");
        return new QRCode(generatedCode);
    }

    private boolean isGeneratedCodeIsNotUnique(String code) {
        return qrCodeService.getByData(code).isPresent();
    }
}
