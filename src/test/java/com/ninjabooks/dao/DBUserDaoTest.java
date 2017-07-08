package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.User;
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
public class DBUserDaoTest
{
    @Autowired
    private UserDao userDao;
    private static final String NAME = "harry_potta";

    private static final String EMAIL = "harry_potta@gmail.com";
    private static final String PASSWORD = "kamehameha";

    private User user;
    private User nullUser = null;

    @Before
    public void setUp() throws Exception {
        this.user = new User(NAME, EMAIL, PASSWORD);
    }

    @Test
    public void testAddUser() throws Exception {
        userDao.add(user);

        User actual = userDao.getAll().findFirst().get();
        assertThat(actual.getId()).isEqualTo(user.getId());
    }

    @Test
    public void testDeleteByEnityUser() throws Exception {
        userDao.add(user);
        userDao.delete(user);

        assertThat(userDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteUserNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userDao.delete(nullUser))
            .withNoCause();
    }

    @Test
    public void testUpdateUser() throws Exception {
        User userBeforeUpdate = user;
        userDao.add(userBeforeUpdate);

        String newName = "Peter";
        userBeforeUpdate.setName(newName);
        userDao.update(userBeforeUpdate);

        User afterUpdate = userDao.getAll().findFirst().get();

        assertThat(afterUpdate.getName()).isEqualTo(newName);
    }


    @Test
    public void testUpdateUserNotExist() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userDao.update(nullUser))
            .withNoCause();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        userDao.add(user);

        User actual = userDao.getAll().findFirst().get();
        assertThat(actual.getId()).isEqualTo(user.getId());
    }

    @Test
    public void testGetUserByName() throws Exception {
        userDao.add(user);

        User actual = userDao.getByName("harry_potta");

        assertThat(actual.getName()).isEqualTo(user.getName());
    }

    @Test
    public void testGetUserByNameNotExistShouldReturnNull() throws Exception {
        assertThat(userDao.getByName("TEST")).isNull();
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        userDao.add(user);

        User actual = userDao.getByEmail(user.getEmail());

        assertThat(actual.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testGetUserEmailWhichNotExistShouldReturnNull() throws Exception {
        assertThat(userDao.getByEmail("TEST")).isNull();
    }
}
