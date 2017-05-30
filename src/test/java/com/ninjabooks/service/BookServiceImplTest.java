package com.ninjabooks.service;

import com.ninjabooks.dao.BookDao;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.util.CodeGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookServiceImplTest
{
    private static final String AUTHOR ="J. Bloch";
    private static final String TITLE = "Effective Java";
    private static final String ISBN = "978-0321356680";

    @Mock
    private BookDao bookDaoMock;

    @Mock
    private QRCodeDao qrCodeDao;

    @Mock
    private CodeGenerator codeGeneratorMock;

    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.bookService = new BookServiceImpl(bookDaoMock, qrCodeDao, codeGeneratorMock);
    }

    @Test
    public void testAddNewBookShouldGenerateQRCode() throws Exception {
        Book book = new Book(TITLE, AUTHOR, ISBN);

        when(codeGeneratorMock.generateCode(book)).thenReturn("");

        bookService.addBook(book);

        verify(codeGeneratorMock, atLeastOnce()).generateCode(any(Book.class));
    }
}
