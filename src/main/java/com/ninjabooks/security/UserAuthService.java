package com.ninjabooks.security;

import com.ninjabooks.dao.UserDao;
import com.ninjabooks.domain.User;
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
    private final UserDao userDao;

    @Autowired
    public UserAuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByEmail(username);

        if (user == null)
            throw new UsernameNotFoundException("User not found");

        return new SpringSecurityUser(
            user.getId(),
            user.getName(),
            user.getPassword(),
            user.getEmail(),
            user.getLastPasswordReset(),
            AuthorityUtils.createAuthorityList("USER")
        );
    }
}
