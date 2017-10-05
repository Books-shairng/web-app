package com.ninjabooks.service.rest.lend;

import com.ninjabooks.service.dao.borrow.BorrowService;
import com.ninjabooks.service.dao.qrcode.QRCodeService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;

import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class BookRentalServiceImplTest
{
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BorrowService borrowServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private QRCodeService qrCodeServiceMock;

    private BookRentalService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new BookRentalServiceImpl(borrowServiceMock, userServiceMock, qrCodeServiceMock);
    }

    @Test
    public void testRentBookShouldPerformSuccessfullBorrow() throws Exception {
        when(userServiceMock.getById(DomainTestConstants.ID)).thenReturn(Optional.ofNullable(DomainTestConstants.USER));
        sut.rentBook(DomainTestConstants.ID, DomainTestConstants.DATA);
    }
}
