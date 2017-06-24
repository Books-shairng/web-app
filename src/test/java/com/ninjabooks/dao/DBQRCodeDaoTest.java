package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.QRCode;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBQRCodeDaoTest
{
    @Autowired
    private QRCodeDao qrCodeDao;

    private static final String DATA = "12345";

    private QRCode qrCode;

    @Before
    public void setUp() throws Exception {
        this.qrCode = new QRCode(DATA);
    }

    @Test
    public void testAddQRCode() throws Exception {
        qrCodeDao.add(qrCode);

        QRCode actual = qrCodeDao.getByData(qrCode.getData());
        assertThat(actual).isEqualTo(qrCode);
    }

    @Test
    public void testGetAllQrCodesShouldReturnsAllRecords() throws Exception {
        qrCodeDao.add(qrCode);

        QRCode actual = qrCodeDao.getAll().findFirst().get();

        assertThat(actual.getId()).isEqualTo(actual.getId());
    }

    @Test
    public void testGetByData() throws Exception {
        qrCodeDao.add(qrCode);

        String data = qrCode.getData();
        assertThat(qrCodeDao.getByData(data)).isEqualTo(qrCode.getData());
    }

    @Test
    public void testGetDataWhichNotExistShouldReturnNull() throws Exception {
        assertThat(qrCodeDao.getByData("TEST")).isNull();
    }

    @Test
    public void testUpdateQRCode() throws Exception {
        qrCodeDao.add(qrCode);

        QRCode beforeUpdate = qrCodeDao.getById(1L);

        String newData = "54312";
        beforeUpdate.setData(newData);
        Session currentSession = qrCodeDao.getCurrentSession();
        currentSession.getTransaction().begin();
        qrCodeDao.update(beforeUpdate);
        currentSession.getTransaction().commit();

        QRCode afterUpdate = qrCodeDao.getAll().findFirst().get();

        assertThat(afterUpdate.getData()).isEqualTo(newData);

    }

    @Test
    public void testUpdateQRCodeNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> qrCodeDao.update(null))
            .withNoCause();
    }

    @Test
    public void testDeleteQRCode() throws Exception {
        qrCodeDao.add(qrCode);
        qrCodeDao.delete(qrCode);

        assertThat(qrCodeDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteQRCodeWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> qrCodeDao.delete(null))
            .withNoCause();
    }
}
