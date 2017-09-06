package com.ninjabooks.dao;

import com.ninjabooks.configuration.DBConnectConfig;
import com.ninjabooks.configuration.TestAppContextInitializer;
import com.ninjabooks.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ContextConfiguration(classes = DBConnectConfig.class,
    initializers = TestAppContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBUserDaoIT
{
    private static final String NAME = "Harry Potta";
    private static final String EMAIL = "harry_potta@gmail.com";
    private static final String PASSWORD = "kamehameha";
    private static final User USER = new User(NAME, EMAIL, PASSWORD);
    private static final String[] IGNORED_FILEDS = {"queues", "borrows", "histories"};
    private static final String NEW_NAME = "Peter Datov";

    @Autowired
    private UserDao sut;

    @Test
    public void testAddUser() throws Exception {
        sut.add(USER);
        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).containsExactly(USER);
    }

    @Test
    public void testDeleteByEnityUser() throws Exception {
        sut.add(USER);
        sut.delete(USER);

        assertThat(sut.getAll()).isEmpty();
    }

    @Test
    public void testUpdateUser() throws Exception {
        User userBeforeUpdate = USER;
        sut.add(userBeforeUpdate);

        userBeforeUpdate.setName(NEW_NAME);
        sut.update(userBeforeUpdate);

        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).containsExactly(USER);
    }


    @Test
    public void testGetUserByEmailWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> sut.getByEmail(EMAIL))
            .withNoCause();
    }

    @Test
    public void testGetUserByNameWhichNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(EntityNotFoundException.class).isThrownBy(() -> sut.getByName(NAME))
            .withNoCause();
    }

    @Test
    public void testGetUserByIDWhichNotExistShouldThrowsException() throws Exception {
        sut.getById(1L);
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        sut.add(USER);
        sut.add(USER);

        Stream<User> actual = sut.getAll();

        assertThat(actual).usingElementComparatorIgnoringFields(IGNORED_FILEDS).contains(USER, USER);
    }

    @Test
    public void testGetUserByName() throws Exception {
        sut.add(USER);

        User actual = sut.getByName(NAME);

        assertThat(actual.getName()).isEqualTo(USER.getName());
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        sut.add(USER);

        User actual = sut.getByEmail(EMAIL);

        assertThat(actual.getEmail()).isEqualTo(USER.getEmail());
    }

}
