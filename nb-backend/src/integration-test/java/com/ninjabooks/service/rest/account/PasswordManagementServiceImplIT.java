package com.ninjabooks.service.rest.account;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.management.ManagementException;

import static com.ninjabooks.util.constants.DomainTestConstants.ENCRYPTED_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class PasswordManagementServiceImplIT
{
    private static final String UNIQUE_PASSWORD = "UnQuQSDasXd@!$!@;";

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordManagementService sut;

    @Test
    public void testChangePassworShouldSucceed() throws Exception {
        sut.change(ID, UNIQUE_PASSWORD);
        Stream<User> users = userDao.getAll();

        assertThat(users).extracting(User::getPassword).isNotEqualTo(ENCRYPTED_PASSWORD);
    }

    @Test
    public void testChangePasswordShouldThrowsExceptionWhenPasswordIsNotUnique() throws Exception {
        assertThatExceptionOfType(ManagementException.class)
            .isThrownBy(() -> sut.change(ID, PLAIN_PASSWORD))
            .withNoCause()
            .withMessageContaining("not unique");
    }
}
