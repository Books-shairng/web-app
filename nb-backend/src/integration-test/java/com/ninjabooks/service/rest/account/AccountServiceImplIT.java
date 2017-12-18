package com.ninjabooks.service.rest.account;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.user.UserAlreadyExistException;
import com.ninjabooks.util.constants.DomainTestConstants;

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
import static org.assertj.core.api.Assertions.tuple;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceImplIT
{
    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountService sut;

    @Test
    public void testCreateUserShouldCreateNewUser() throws Exception {
        sut.createUser(DomainTestConstants.USER);
        Stream<User> actual = userDao.getAll();

        assertThat(actual).extracting("name", "email")
            .contains(tuple(DomainTestConstants.NAME, DomainTestConstants.EMAIL));
    }

    @Test
    @Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateUserShouldFailedWhenUserAlreadyExist() throws Exception {
        assertThatExceptionOfType(UserAlreadyExistException.class)
            .isThrownBy(() -> sut.createUser(DomainTestConstants.USER))
            .withMessageContaining("already exist in database")
            .withNoCause();
    }
}
