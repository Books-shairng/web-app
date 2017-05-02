package com.ninjabooks.dao;

import com.ninjabooks.configuration.HSQLConfig;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class DBUserDaoTest
{
    @Autowired
    private UserDao userDao;

    private List<User> users;
    private TransactionManager transactionManager;

    @Before
    public void setUp() throws Exception {
        transactionManager = new TransactionManager(userDao.getCurrentSession());
        users = createRecords();
        transactionManager.beginTransaction();
    }

    private List<User> createRecords() {
        User firstUser = new User();
        firstUser.setName("harry_potta");
        firstUser.setEmail("harry_potta@gmail.com");
        firstUser.setPassword("kamehameha");

        User secondUser = new User();
        secondUser.setName("alfred");
        secondUser.setEmail("alfred@buziaczek.com");
        secondUser.setPassword("123");

        List<User> users = new ArrayList<>();
        users.add(firstUser);
        users.add(secondUser);

        return users;
    }

    @Test
    public void testAddUser() throws Exception {
        userDao.add(users.get(0));

        assertThat(userDao.getAll()).containsExactly(users.get(0));
    }

    @Test
    public void testDeleteUser() throws Exception {
        userDao.add(users.get(0));
        userDao.delete(users.get(0).getId());

        assertThat(userDao.getAll()).isEmpty();
    }

    @Test
    public void testDeleteUserNotExistShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userDao.delete(555L))
            .withNoCause();
    }

    @Test
    public void testUpdateUser() throws Exception {
        User userBeforeUpdate = users.get(0);
        userDao.add(userBeforeUpdate);

        String newName = "Peter";
        userBeforeUpdate.setName(newName);
        userDao.update(userBeforeUpdate.getId());

        User afterUpdate = userDao.getAll().findFirst().get();

        assertThat(afterUpdate.getName()).isEqualTo(newName);
    }

    @Test
    public void testUpdateUserNotExist() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userDao.update(555L))
            .withNoCause();
    }

    @Test
    public void testGetAllShouldReturnsAllRecord() throws Exception {
        users.forEach(user -> userDao.add(user));

        assertThat(userDao.getAll()).containsExactly(users.get(0), users.get(1));
    }

    @Test
    public void testGetUserByName() throws Exception {
        users.forEach(user -> userDao.add(user));

        User actual = userDao.getByName("alfred");

        assertThat(actual).isEqualTo(users.get(1));
    }

    @Test
    public void testGetUserByNameNotExistShouldReturnNull() throws Exception {
        assertThat(userDao.getByName("TEST")).isNull();
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        users.forEach(user -> userDao.add(user));

        User actual = userDao.getByEmail(users.get(1).getEmail());

        assertThat(actual).isEqualTo(users.get(1));
    }

    @Test
    public void testGetUserEmailWhichNotExistShouldReturnNull() throws Exception {
        assertThat(userDao.getByEmail("TEST")).isNull();
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}
