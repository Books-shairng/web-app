package com.ninjabooks.dao.db;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DBUserDaoIT
{
    private static final String NEW_NAME = "Peter Datov";
    private static final String[] IGNORED_FILEDS = {"queues", "borrows", "histories", "lastPasswordReset"};

    @Autowired
    private UserDao sut;

    @Test
    public void testAddUser() throws Exception {
        sut.add(DomainTestConstants.USER);
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).containsExactly(DomainTestConstants.USER);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteByEnityUser() throws Exception {
        sut.delete(DomainTestConstants.USER);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        Optional<User> actual = sut.getById(DomainTestConstants.ID);

        hasUserContainsSatisfyingValues(actual);
    }

    @Test
    public void testGetByIdEnityWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getById(DomainTestConstants.ID);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetUserByNameWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getByName(DomainTestConstants.NAME);

        assertThat(actual).isEmpty();
    }

    @Test
    public void testGetUserByEmailWhichNotExistShouldReturnEmptyOptional() throws Exception {
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        assertThat(actual).isEmpty();
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).contains(DomainTestConstants.USER, DomainTestConstants.USER);
    }

    @Test
    public void testGetAllWhenDbIsEmptyShouldReturnsEmptyStream() throws Exception {
        Stream<User> actual = sut.getAll();

        assertThat(actual).isEmpty();
    }


    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUserByName() throws Exception {
        Optional<User> actual = sut.getByName(DomainTestConstants.NAME);

        hasUserContainsSatisfyingValues(actual);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUserByEmail() throws Exception {
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        hasUserContainsSatisfyingValues(actual);
    }

    @Test
    @Sql(value = "classpath:dao_import.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateUser() throws Exception {
        User entityToUpdate = createFreshEntity();
        entityToUpdate.setName(NEW_NAME);

        sut.update(entityToUpdate);
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).containsExactly(entityToUpdate);
    }

    private User createFreshEntity() {
        User entityToUpdate = new User(DomainTestConstants.NAME, DomainTestConstants.EMAIL, DomainTestConstants.PASSWORD);
        entityToUpdate.setId(DomainTestConstants.ID);

        return entityToUpdate;
    }

    private void hasUserContainsSatisfyingValues(Optional<User> actual) {
        assertThat(actual).hasValueSatisfying(user -> {
            assertThat(user.getEmail()).isEqualTo(DomainTestConstants.EMAIL);
            assertThat(user.getName()).isEqualTo(DomainTestConstants.NAME);
            assertThat(user.getPassword()).isEqualTo(DomainTestConstants.PASSWORD);
        });
    }
}
