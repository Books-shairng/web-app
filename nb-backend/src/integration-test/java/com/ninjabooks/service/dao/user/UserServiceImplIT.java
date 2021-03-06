package com.ninjabooks.service.dao.user;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.domain.User;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import javax.transaction.Transactional;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Transactional
@Sql(value = "classpath:sql_query/it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class UserServiceImplIT extends AbstractBaseIT
{
    private static final String CUSTOM_NAME = "Jan Kowalski";
    private static final String CUSTOM_EMAIL = "jan@kowalski.pl";

    @Autowired
    private UserService sut;

    @Test
    public void testGetByNameShouldReturnOptionalWithUser() throws Exception {
        Optional<User> actual = sut.getByName(NAME);

        assertSoftly(softly -> {
            assertThat(actual).hasValueSatisfying(this::isEmailEqual);
            assertThat(actual).hasValueSatisfying(this::isNameEqual);
            assertThat(actual).hasValueSatisfying(this::isPasswordEqual);
        });
    }

    @Test
    public void testGetByEmailShouldReturnOptionalWithUser() throws Exception {
        Optional<User> actual = sut.getByEmail(EMAIL);

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
        return user.getPassword().equals(PLAIN_PASSWORD);
    }

    private boolean isNameEqual(User user) {
        return user.getName().equals(NAME);
    }

    private boolean isEmailEqual(User user) {
        return user.getEmail().equals(EMAIL);
    }

}
