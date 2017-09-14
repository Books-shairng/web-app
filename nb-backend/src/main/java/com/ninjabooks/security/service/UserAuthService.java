package com.ninjabooks.security.service;

import com.ninjabooks.domain.User;
import com.ninjabooks.security.user.SpringSecurityUserFactory;
import com.ninjabooks.service.dao.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * In this service is performed basic user authorization
 *
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@Service
@Transactional
public class UserAuthService implements UserDetailsService
{


    private final UserService userService;

    @Autowired
    public UserAuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return SpringSecurityUserFactory.makeSecurityUser(user);
    }
}
