package com.ninjabooks.service.rest.account;

import com.ninjabooks.domain.BaseEntity;
import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.management.ManagementException;
import com.ninjabooks.service.dao.generic.GenericService;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.entity.EntityUtilsWrapper;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;
import static com.ninjabooks.util.constants.DomainTestConstants.ID;
import static com.ninjabooks.util.constants.DomainTestConstants.NAME;
import static com.ninjabooks.util.constants.DomainTestConstants.PLAIN_PASSWORD;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class PasswordManagementServiceImplTest
{
    private static final String UNIQUE_PASSWORD = "ThIs!@#PaSSword";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private UserService userServiceMock;

    @Mock
    private EntityUtilsWrapper entityWrapperMock;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private PasswordManagementService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new PasswordManagementServiceImpl(userServiceMock, passwordEncoder, entityWrapperMock);
        when(entityWrapperMock.getEnity(userServiceMock, ID)).thenReturn(createUserWithEncryptedPassword());
    }

    @Test
    public void testChangePasswordShouldSucceed() throws Exception {
        sut.change(ID, UNIQUE_PASSWORD);

        verify(entityWrapperMock, atLeastOnce()).getEnity((GenericService<BaseEntity, Long>) any(), anyLong());
        verify(userServiceMock, atLeastOnce()).update(any());
    }

    @Test
    public void testChangePasswordShouldThrowsExceptionWhenPasswordNotUnique() throws Exception {
        assertThatExceptionOfType(ManagementException.class)
            .isThrownBy(() -> sut.change(ID, PLAIN_PASSWORD))
            .withNoCause()
            .withMessageContaining("not unique");

        verify(entityWrapperMock, atLeastOnce()).getEnity((GenericService<BaseEntity, Long>) any(), anyLong());
    }

    private User createUserWithEncryptedPassword() {
        String encodedPassword = passwordEncoder.encode(PLAIN_PASSWORD);
        return new User(NAME, EMAIL, encodedPassword);
    }
}
