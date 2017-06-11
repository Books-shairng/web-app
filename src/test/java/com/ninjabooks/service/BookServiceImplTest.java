package com.ninjabooks.service;

import com.ninjabooks.dao.db.DBBookDao;
import com.ninjabooks.dao.db.DBQRCodeDao;
import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.error.QRCodeException;
import com.ninjabooks.util.CodeGenerator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class BookServiceImplTest
{
    private static final String AUTHOR ="J. Bloch";
    private static final String TITLE = "Effective Java";
    private static final String ISBN = "978-0321356680";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private DBBookDao bookDaoMock;

    @Mock
    private DBQRCodeDao qrCodeDaoMock;

    @Mock
    private CodeGenerator codeGeneratorMock;

    private BookService bookService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.bookService = new BookServiceImpl(bookDaoMock, qrCodeDaoMock, codeGeneratorMock);
    }

    @Test
    public void testAddNewBookShouldGenerateQRCode() throws Exception {
        Book book = new Book(TITLE, AUTHOR, ISBN);

        //given
        when(codeGeneratorMock.generateCode()).thenReturn("generated code");

        //when
        String generatedCode = bookService.addBook(book);

        //then
        verify(codeGeneratorMock, atLeastOnce()).generateCode();

        assertThat(generatedCode).isEqualTo("generated code");

    }

    @Test
    public void testAddNewBookShouldThrowsException_whenUnableToGenerateNewQrCode() throws Exception {
        Book book = new Book(TITLE, AUTHOR, ISBN);
        QRCode qrCode = new QRCode("data");

        //given
        when(qrCodeDaoMock.getByData(ArgumentMatchers.any())).thenReturn(qrCode);

        //when
        exception.expect(QRCodeException.class);

        //then
        bookService.addBook(book);

        verify(qrCodeDaoMock, atLeastOnce()).getByData("data");
    }

}
