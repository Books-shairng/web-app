package com.ninjabooks.service.dao.qrcode;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.constants.DomainTestConstants;

import javax.transaction.Transactional;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class QRCodeServiceImplIT extends AbstractBaseIT
{
    private static final String CUSTMOM_DATA = "123456ngkfcz";

    @Autowired
    private QRCodeDao sut;

    @Test
    public void testGetByDataShouldReturnOptionalWithExpectedQRCode() throws Exception {
        Optional<QRCode> actual = sut.getByData(DomainTestConstants.DATA);

        assertThat(actual).contains(DomainTestConstants.QR_CODE_FULL);
    }

    @Test
    public void testGetByDataShouldReturnEmptyOptionalWhenQRCodeNotFound() throws Exception {
        Optional<QRCode> actual = sut.getByData(CUSTMOM_DATA);

        assertThat(actual).isEmpty();
    }
}
