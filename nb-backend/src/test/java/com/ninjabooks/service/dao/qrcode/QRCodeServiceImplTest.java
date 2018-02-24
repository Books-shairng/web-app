package com.ninjabooks.service.dao.qrcode;

import com.ninjabooks.dao.GenericDao;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.CommonUtils;

import static com.ninjabooks.util.constants.DomainTestConstants.DATA;
import static com.ninjabooks.util.constants.DomainTestConstants.QR_CODE;

import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QRCodeServiceImplTest
{
    private static final Optional<QRCode> EXPECTED_OPTIONAL = CommonUtils.asOptional(QR_CODE);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private GenericDao<QRCode, Long> genericDaoMock;

    @Mock
    private QRCodeDao qrCodeDaoMock;

    private QRCodeService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new QRCodeServiceImpl(genericDaoMock, qrCodeDaoMock);
    }

    @Test
    public void testGetByDataShouldReturnOptional() throws Exception {
        when(qrCodeDaoMock.getByData(DATA)).thenReturn(EXPECTED_OPTIONAL);
        Optional<QRCode> actual = sut.getByData(DATA);

        assertThat(actual).contains(QR_CODE);
        verify(qrCodeDaoMock, atLeastOnce()).getByData(any());
    }

    @Test
    public void testGetByDataWhenEmptyShouldReturnEmptyOptional() throws Exception {
        when(qrCodeDaoMock.getByData(DATA)).thenReturn(Optional.empty());
        Optional<QRCode> actual = sut.getByData(DATA);

        assertThat(actual).isEmpty();
        verify(qrCodeDaoMock, atLeastOnce()).getByData(any());
    }
}
