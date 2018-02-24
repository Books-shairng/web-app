package com.ninjabooks.service.rest.book;

import com.ninjabooks.dto.BookDto;
import com.ninjabooks.error.exception.qrcode.QRCodeException;
import com.ninjabooks.json.book.BookInfo;
import com.ninjabooks.service.dao.book.BookDaoService;
import com.ninjabooks.service.dao.qrcode.QRCodeService;
import com.ninjabooks.util.QRCodeGenerator;

import static com.ninjabooks.util.constants.DomainTestConstants.AUTHOR;
import static com.ninjabooks.util.constants.DomainTestConstants.BOOK;
import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.ISBN;
import static com.ninjabooks.util.constants.DomainTestConstants.QR_CODE;
import static com.ninjabooks.util.constants.DomainTestConstants.TITLE;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookRestServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private BookDaoService bookServiceMock;

    @Mock
    private QRCodeService qrCodeServiceMock;

    @Mock
    private QRCodeGenerator codeGeneratorMock;

    private BookRestService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookRestServiceImpl(bookServiceMock, qrCodeServiceMock, codeGeneratorMock);
    }

    @Test
    public void testAddNewBookShouldGenerateUniqueQRCodeAndAddBookIntoDB() throws Exception {
        when(codeGeneratorMock.generateCode()).thenReturn(DATA);
        String actual = sut.addBook(BOOK);

        assertThat(actual).isEqualTo(DATA);
        verify(codeGeneratorMock, atLeastOnce()).generateCode();
    }

    @Test
    public void testAddNewBookWhenUnableToGenerateUniqueCodeShouldThrowsException() throws Exception {
        when(qrCodeServiceMock.getByData(any())).thenReturn(Optional.of(QR_CODE));
        assertThatExceptionOfType(QRCodeException.class).isThrownBy(() -> sut.addBook(BOOK))
            .withNoCause();

        verify(qrCodeServiceMock, atLeastOnce()).getByData(any());
    }

    @Test
    public void testGetBookInfoShouldReturnExpectedBook() throws Exception {
        when(bookServiceMock.getById(ID)).thenReturn(Optional.of(BOOK));
        BookInfo bookInfo = sut.getBookInfo(ID);
        BookDto actual = bookInfo.getBookDto();

        assertThat(actual).extracting("author", "title", "isbn").contains(AUTHOR, TITLE, ISBN);
    }
}
