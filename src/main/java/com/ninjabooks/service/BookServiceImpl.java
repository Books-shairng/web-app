package com.ninjabooks.service;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.CodeGenerator;
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
    private final BookDao bookDao;
    private final QRCodeDao qrCodeDao;
    private final CodeGenerator codeGenerator;

    @Autowired
    public BookServiceImpl(BookDao bookDao, QRCodeDao qrCodeDao, CodeGenerator codeGenerator) {
        this.bookDao = bookDao;
        this.qrCodeDao = qrCodeDao;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public String addBook(Book book) {
        String generatedCode = generateCode(book);

        QRCode qrCode = new QRCode(generatedCode);
        qrCodeDao.add(qrCode);

        book.setQRCode(qrCode);

        bookDao.add(book);

        return qrCode.getData();
    }

    private String generateCode(Book book) {
        return codeGenerator.generateCode(book);
    }
}
