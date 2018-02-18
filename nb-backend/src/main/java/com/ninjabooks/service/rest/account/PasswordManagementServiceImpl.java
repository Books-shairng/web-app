package com.ninjabooks.service.rest.account;

import com.ninjabooks.domain.User;
import com.ninjabooks.error.exception.management.ManagementException;
import com.ninjabooks.service.dao.user.UserService;
import com.ninjabooks.util.entity.EntityUtilsWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class PasswordManagementServiceImpl implements PasswordManagementService
{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EntityUtilsWrapper entityUtils;

    @Autowired
    public PasswordManagementServiceImpl(UserService userService,
                                         PasswordEncoder passwordEncoder,
                                         EntityUtilsWrapper entityUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.entityUtils = entityUtils;
    }

    @Override
    public void change(Long id, String password) throws ManagementException {
        User user = getUser(id);

        if (isNewPasswordNotUnique(user.getPassword(), password)) {
            throw new ManagementException("New password is not unique");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        userService.update(user);
    }

    private User getUser(Long id) {
        return entityUtils.getEnity(userService, id);
    }

    private boolean isNewPasswordNotUnique(String oldPassword, String newPassword) {
        return passwordEncoder.matches(newPassword, oldPassword);
    }
}
