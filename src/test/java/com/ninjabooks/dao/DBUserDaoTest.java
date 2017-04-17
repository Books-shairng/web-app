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

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ContextConfiguration(classes = HSQLConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
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
        userDao.delete(8L);

        assertThat(userDao.getAll()).isEmpty();
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
    public void testGetUserByEmail() throws Exception {
        users.forEach(user -> userDao.add(user));

        User actual = userDao.getByEmail(users.get(1).getEmail());

        assertThat(actual).isEqualTo(users.get(1));
    }

    @After
    public void tearDown() throws Exception {
        transactionManager.rollback();
    }
}