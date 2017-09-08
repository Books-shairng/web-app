package com.ninjabooks.dao;

import com.ninjabooks.configuration.DBConnectConfig;
import com.ninjabooks.configuration.TestAppContextInitializer;
import com.ninjabooks.domain.QRCode;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.NoResultException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Ignore
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = DBConnectConfig.class,
    initializers = TestAppContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBQRCodeDaoIT
{
    private static final String DATA = "12345";
    private static final QRCode QR_CODE = new QRCode(DATA);
    private static final String NEW_DATA = "54312";

    @Autowired
    private QRCodeDao qrCodeDao;

    @Test
    public void testAddQRCode() throws Exception {
        qrCodeDao.add(QR_CODE);

        Stream<QRCode> actual = qrCodeDao.getAll();
        assertThat(actual).containsExactly(QR_CODE);
    }

    @Test
    public void testGetByData() throws Exception {
        qrCodeDao.add(QR_CODE);

        QRCode actual = qrCodeDao.getByData(DATA).get();

        assertThat(actual.getData()).isEqualTo(QR_CODE.getData());
    }

    @Test
    public void testGetAllQrCodesShouldReturnsAllRecords() throws Exception {
        qrCodeDao.add(QR_CODE);
        qrCodeDao.add(QR_CODE);

        Stream<QRCode> actual = qrCodeDao.getAll();

        assertThat(actual).containsExactly(QR_CODE, QR_CODE);
    }

    @Test
    public void testGetDataWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(NoResultException.class).isThrownBy(() -> qrCodeDao.getByData(DATA))
            .withNoCause();
    }

    @Test
    public void testUpdateQRCode() throws Exception {
        QRCode beforeUpdate = QR_CODE;
        qrCodeDao.add(QR_CODE);
        beforeUpdate.setData(NEW_DATA);

        qrCodeDao.update(beforeUpdate);

        QRCode afterUpdate = qrCodeDao.getAll().findFirst().get();

        assertThat(afterUpdate.getData()).isEqualTo(QR_CODE.getData());
    }


    @Test
    public void testUpdateQRCodeWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> qrCodeDao.update(null))
            .withNoCause();
    }

    @Test
    public void testDeleteQRCode() throws Exception {
        qrCodeDao.add(QR_CODE);
        qrCodeDao.delete(QR_CODE);

        assertThat(qrCodeDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteQRCodeWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> qrCodeDao.delete(null))
            .withNoCause();
    }

}
