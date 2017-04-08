package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
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

        QRCode testQRCode = new QRCode();
        testQRCode.setData("alalala");

        qrCodes.add(testQRCode);
    }

    @Test
    public void testGetByData() throws Exception {
        qrCodeDao.add(qrCodes.get(0));

        QRCode actual = qrCodeDao.getByData(qrCodes.get(0).getData());
        assertThat(actual).isEqualTo(qrCodes.get(0));
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}