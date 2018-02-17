package com.ninjabooks.dao.db;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.QRCodeDao;
import com.ninjabooks.domain.QRCode;
import com.ninjabooks.util.constants.DomainTestConstants;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBQRCodeDaoIT extends AbstractBaseIT
{
    private static final String NEW_DATA = "abcde12345";

    @Autowired
    private QRCodeDao sut;

    @Test
    public void testAddQRCode() throws Exception {
        sut.add(DomainTestConstants.QR_CODE);
        Stream<QRCode> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.QR_CODE);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteQRCode() throws Exception {
        sut.delete(DomainTestConstants.QR_CODE);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<QRCode> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).contains(DomainTestConstants.QR_CODE);
    }

    @Test
    public void testGetByIdWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<QRCode> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetByData() throws Exception {
        Optional<QRCode> actual = sut.getByData(DomainTestConstants.DATA);

        assertThat(actual).hasValueSatisfying(qr -> {
            assertThat(qr.getId()).isEqualTo(DomainTestConstants.ID);
            assertThat(qr.getData()).isEqualTo(DomainTestConstants.DATA);
        });
    }

    @Test
    public void testGetDataWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<QRCode> actual = sut.getByData(DomainTestConstants.DATA);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllQrCodesShouldReturnsAllRecords() throws Exception {
        Stream<QRCode> actual = sut.getAll();

        assertThat(actual).containsExactly(DomainTestConstants.QR_CODE);
    }

    @Test
    public void testGetAllWhenDBIsEmptyShouldReturnEmptyStream() throws Exception {
        Stream<QRCode> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateQRCode() throws Exception {
        QRCode entityToUpdate = createFreshEntity();
        entityToUpdate.setData(NEW_DATA);

        sut.update(entityToUpdate);
        Stream<QRCode> afterUpdate = sut.getAll();

        assertThat(afterUpdate).containsExactly(entityToUpdate);
    }

    private QRCode createFreshEntity() {
        QRCode entityToUpdate = new QRCode(DomainTestConstants.DATA);
        entityToUpdate.setId(DomainTestConstants.ID);

        return entityToUpdate;
    }
}
