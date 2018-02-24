package com.ninjabooks.dao.db;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ENCRYPTED_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;
import static com.ninjabooks.util.constants.DomainTestConstants.USER;
import static com.ninjabooks.util.constants.DomainTestConstants.USER_ENCRYPT_PASS;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
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
public class DBUserDaoIT extends AbstractBaseIT
{
    private static final String NEW_NAME = "Peter Datov";
    private static final String[] IGNORED_FILEDS = {"queues", "borrows", "histories", "comments", "lastPasswordReset"};

    @Autowired
    private UserDao sut;

    @Test
    public void testAddUser() throws Exception {
        sut.add(USER);
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).containsExactly(USER);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteByEnityUser() throws Exception {
        sut.delete(USER);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<User> actual = sut.getById(ID);

        hasUserContainsSatisfyingValues(actual);
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getById(ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetUserByNameWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getByName(NAME);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetUserByEmailWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getByEmail(EMAIL);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).contains(USER_ENCRYPT_PASS);
    }

    @Test
    public void testGetAllWhenDbIsEmptyShouldReturnsEmptyStream() throws Exception {
        Stream<User> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }


    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUserByName() throws Exception {
        Optional<User> actual = sut.getByName(NAME);

        hasUserContainsSatisfyingValues(actual);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUserByEmail() throws Exception {
        Optional<User> actual = sut.getByEmail(EMAIL);

        hasUserContainsSatisfyingValues(actual);
    }

    @Test
    @Sql(value = "classpath:sql_query/dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateUser() throws Exception {
        User entityToUpdate = createFreshEntity();
        entityToUpdate.setName(NEW_NAME);

        sut.update(entityToUpdate);
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).containsExactly(entityToUpdate);
    }

    private User createFreshEntity() {
        User entityToUpdate = new User(NAME, EMAIL, PLAIN_PASSWORD);
        entityToUpdate.setId(ID);

        return entityToUpdate;
    }

    private void hasUserContainsSatisfyingValues(Optional<User> actual) {
        assertThat(actual).hasValueSatisfying(user -> {
            assertThat(user.getEmail()).isEqualTo(EMAIL);
            assertThat(user.getName()).isEqualTo(NAME);
            assertThat(user.getPassword()).isEqualTo(ENCRYPTED_PASSWORD);
        });
    }
}
