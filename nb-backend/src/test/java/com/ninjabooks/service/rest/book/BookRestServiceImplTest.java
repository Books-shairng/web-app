package com.ninjabooks.service.rest.book;

import com.ninjabooks.error.qrcode.QRCodeException;
import com.ninjabooks.service.dao.book.BookService;
import com.ninjabooks.service.dao.qrcode.QRCodeService;
import com.ninjabooks.util.QRCodeGenerator;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookRestServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BookService bookServiceMock;

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
        when(codeGeneratorMock.generateCode()).thenReturn(DomainTestConstants.DATA);
        String actual = sut.addBook(DomainTestConstants.BOOK);

        assertThat(actual).isEqualTo(DomainTestConstants.DATA);
        verify(codeGeneratorMock, atLeastOnce()).generateCode();
    }

    @Test
    public void testAddNewBookWhenUnableToGenerateUniqueCodeShouldThrowsException() throws Exception {
        when(qrCodeServiceMock.getByData(any())).thenReturn(Optional.of(DomainTestConstants.QR_CODE));
        assertThatExceptionOfType(QRCodeException.class).isThrownBy(() -> sut.addBook(DomainTestConstants.BOOK))
            .withNoCause();

        verify(qrCodeServiceMock, atLeastOnce()).getByData(any());
    }

}
