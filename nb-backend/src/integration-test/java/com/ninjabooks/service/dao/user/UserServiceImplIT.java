package com.ninjabooks.service.dao.user;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.domain.User;
import com.ninjabooks.util.constants.DomainTestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceImplIT
{
    private static final String CUSTOM_NAME = "Jan Kowalski";
    private static final String CUSTOM_EMAIL = "jan@kowalski.pl";

    @Autowired
    private UserService sut;

    @Test
    public void testGetByNameShouldReturnOptionalWithUser() throws Exception {
        Optional<User> actual = sut.getByName(DomainTestConstants.NAME);

        assertSoftly(softly -> {
            assertThat(actual).hasValueSatisfying(this::isEmailEqual);
            assertThat(actual).hasValueSatisfying(this::isNameEqual);
            assertThat(actual).hasValueSatisfying(this::isPasswordEqual);
        });
    }

    @Test
    public void testGetByEmailShouldReturnOptionalWithUser() throws Exception {
        Optional<User> actual = sut.getByEmail(DomainTestConstants.EMAIL);

        assertSoftly(softly -> {
            assertThat(actual).hasValueSatisfying(this::isEmailEqual);
            assertThat(actual).hasValueSatisfying(this::isNameEqual);
            assertThat(actual).hasValueSatisfying(this::isPasswordEqual);
        });
    }

    @Test
    public void testGetByNameShouldReturnEmptyOptionalWhenUserNotFound() throws Exception {
        Optional<User> actual = sut.getByName(CUSTOM_NAME);

        assertThat(actual).isEmpty();

    }

    @Test
    public void testGetByEmailShouldReturnEmptyOptionalWhenUserNotFound() throws Exception {
        Optional<User> actual = sut.getByEmail(CUSTOM_EMAIL);

        assertThat(actual).isEmpty();
    }

    private boolean isPasswordEqual(User user) {
    return user.getPassword().equals(DomainTestConstants.PASSWORD);
}

    private boolean isNameEqual(User user) {
        return user.getName().equals(DomainTestConstants.NAME);
    }

    private boolean isEmailEqual(User user) {
        return user.getEmail().equals(DomainTestConstants.EMAIL);
    }

}
