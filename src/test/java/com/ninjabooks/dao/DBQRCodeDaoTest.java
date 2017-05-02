package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBQRCodeDaoTest
{
    @Autowired
    private QRCodeDao qrCodeDao;

    private List<QRCode> qrCodes;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        createRecords();
        transactionManager = new TransactionManager(qrCodeDao.getCurrentSession());
        transactionManager.beginTransaction();
    }

    private void createRecords() {
        qrCodes = new ArrayList<>();

        QRCode firstQrcode = new QRCode();
        QRCode secondQrcode = new QRCode();

        firstQrcode.setData("alala");
        secondQrcode.setData("12345");

        qrCodes.add(firstQrcode);
        qrCodes.add(secondQrcode);
    }

    @Test
    public void testAddQRCode() throws Exception {
        qrCodeDao.add(qrCodes.get(0));

        QRCode actual = qrCodeDao.getByData(qrCodes.get(0).getData());
        assertThat(actual).isEqualTo(qrCodes.get(0));
    }

    @Test
    public void testGetAllQrCodesShouldReturnsAllRecords() throws Exception {
        qrCodes.forEach(qrCode -> qrCodeDao.add(qrCode));

        assertThat(qrCodeDao.getAll()).containsExactly(qrCodes.get(0), qrCodes.get(1));
    }

    @Test
    public void testGetByData() throws Exception {
        qrCodes.forEach(qrCode -> qrCodeDao.add(qrCode));

        String data = qrCodes.get(0).getData();
        assertThat(qrCodeDao.getByData(data)).isEqualTo(qrCodes.get(0));
    }

    @Test
    public void testGetDataWhichNotExistShouldReturnNull() throws Exception {
        assertThat(qrCodeDao.getByData("TEST")).isNull();
    }

    @Test
    public void testUpdateQRCode() throws Exception {
        QRCode beforeUpdate = qrCodes.get(0);
        qrCodeDao.add(beforeUpdate);

        String newData = "54312";
        beforeUpdate.setData(newData);
        qrCodeDao.update(beforeUpdate.getId());

        QRCode afterUpdate = qrCodeDao.getAll().findFirst().get();

        assertThat(afterUpdate.getData()).isEqualTo(newData);

    }

    @Test
    public void testUpdateQRCodeNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> qrCodeDao.update(555L))
            .withNoCause();
    }

    @Test
    public void testDeleteQRCode() throws Exception {
        qrCodeDao.add(qrCodes.get(0));

        qrCodeDao.delete(qrCodes.get(0).getId());

        assertThat(qrCodeDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteQRCodeWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> qrCodeDao.delete(555L))
            .withNoCause();
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}
