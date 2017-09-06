package com.ninjabooks.service.rest.book;

import com.ninjabooks.domain.Book;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.error.qrcode.QRCodeException;
import com.ninjabooks.service.dao.book.BookService;
import com.ninjabooks.service.dao.qrcode.QRCodeService;
import com.ninjabooks.util.CodeGenerator;
import com.ninjabooks.util.CommonUtils;
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
    private CodeGenerator codeGeneratorMock;

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
        when(qrCodeServiceMock.getByData(any())).thenReturn(Optional.of(CommonUtils.DATA));
        assertThatExceptionOfType(QRCodeException.class).isThrownBy(() -> sut.addBook(BOOK))
            .withNoCause();

        verify(qrCodeServiceMock, atLeastOnce()).getByData(any());
    }

}
