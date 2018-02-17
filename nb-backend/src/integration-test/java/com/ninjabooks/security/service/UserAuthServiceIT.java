package com.ninjabooks.security.service;

import com.ninjabooks.config.AbstractBaseIT;
import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.util.constants.DomainTestConstants;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserAuthServiceIT extends AbstractBaseIT
{
    @Autowired
    @Qualifier(value = "userAuthService")
    private UserDetailsService sut;

    @Test
    @Sql(value = "classpath:sql_query/it_import.sql", executionPhase = BEFORE_TEST_METHOD)
    public void testLoadUserByUsernameShouldReturnExpectedFields() throws Exception {
        UserDetails details = sut.loadUserByUsername(DomainTestConstants.EMAIL);
        SpringSecurityUser actual = (SpringSecurityUser) details;
        assertThat(actual).extracting("id", "name", "email")
            .containsExactly(DomainTestConstants.ID, DomainTestConstants.NAME, DomainTestConstants.EMAIL);
    }

    @Test
    public void testLoadUserByUsernameShouldThrowsExceptionWhenEmpty() throws Exception {
        assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> sut.loadUserByUsername(DomainTestConstants.EMAIL))
            .withMessageContaining("User not found")
            .withNoCause();
    }
}
