package com.ninjabooks.dao;

import com.ninjabooks.dao.db.DBDaoHelper;
import com.ninjabooks.dao.db.DBQRCodeDao;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.CommonUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.util.db.SpecifiedElementFinder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class DBQRCodeDaoTest
{
    private static final String NEW_DATA = "54312";
    private static final Supplier<Stream<QRCode>> QR_CODE_SUPPLIER = CommonUtils.asSupplier(DomainTestConstants.QR_CODE);
    private static final Optional<QRCode> QR_CODE_OPTIONAL = CommonUtils.asOptional(DomainTestConstants.QR_CODE);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SessionFactory sessionFactoryMock;

    @Mock
    private DBDaoHelper<QRCode> daoHelperMock;

    @Mock
    private Session sessionMock;

    @Mock
    private Query queryMock;

    @Mock
    private SpecifiedElementFinder specifiedElementFinderMock;

    private QRCodeDao sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new DBQRCodeDao(sessionFactoryMock, daoHelperMock, specifiedElementFinderMock);
        when(sessionFactoryMock.openSession()).thenReturn(sessionMock);
        when(sessionMock.createQuery(any(), any())).thenReturn(queryMock);
    }

    @Test
    public void testAddQRCode() throws Exception {
        when(sessionMock.save(any())).thenReturn(DomainTestConstants.ID);
        sut.add(DomainTestConstants.QR_CODE);

        verify(sessionMock, atLeastOnce()).save(any());
    }

    @Test
    public void testDeleteQRCode() throws Exception {
        doNothing().when(daoHelperMock).delete(DomainTestConstants.QR_CODE);
        sut.delete(DomainTestConstants.QR_CODE);

        verify(daoHelperMock, atLeastOnce()).delete(any());
    }

    @Test
    public void testGetById() throws Exception {
        when(sessionMock.get((Class<Object>) any(), any())).thenReturn(DomainTestConstants.QR_CODE);
        Optional<QRCode> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.QR_CODE);
        verify(sessionMock, atLeastOnce()).get((Class<Object>) any(), any());
    }

    @Test
    public void testGetByIdWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<QRCode> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetAllQrCodesShouldReturnsAllRecords() throws Exception {
        when(queryMock.stream()).thenReturn(QR_CODE_SUPPLIER.get());
        Stream<QRCode> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.QR_CODE);
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<QRCode> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetByData() throws Exception {
        when(specifiedElementFinderMock.findSpecifiedElementInDB(any(), any(), any())).thenReturn(QR_CODE_OPTIONAL);
        Optional<QRCode> actual = sut.getByData(DomainTestConstants.DATA);

        assertThat(actual).contains(DomainTestConstants.QR_CODE);
    }

    @Test
    public void testGetDataWhichNotExistShouldReturnNull() throws Exception {
        Optional<QRCode> actual = sut.getByData(DomainTestConstants.DATA);

        assertThat(actual).isNull();
    }

    @Test
    public void testUpdateQRCode() throws Exception {
        QRCode beforeUpdate = DomainTestConstants.QR_CODE;
        beforeUpdate.setData(NEW_DATA);

        doNothing().when(daoHelperMock).update(beforeUpdate);
        sut.update(beforeUpdate);

        verify(daoHelperMock, atLeastOnce()).update(any());
    }

}
