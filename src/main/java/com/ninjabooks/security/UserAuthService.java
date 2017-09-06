package com.ninjabooks.security;

import com.ninjabooks.domain.User;
import com.ninjabooks.service.dao.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
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
    private static final String ROLE_PREFIX = "ROLE_";

    private final UserService userService;

    @Autowired
    public UserAuthService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new SpringSecurityUser(
            user.getId(),
            user.getName(),
            user.getPassword(),
            user.getEmail(),
            user.getLastPasswordReset(),
            AuthorityUtils.createAuthorityList(ROLE_PREFIX + user.getAuthoritiy())
        );
    }
}
