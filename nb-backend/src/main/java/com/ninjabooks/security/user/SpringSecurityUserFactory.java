package com.ninjabooks.security.user;

import com.ninjabooks.domain.User;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SpringSecurityUserFactory
{
    private static final String ROLE_PREFIX = "ROLE_";

    public static SpringSecurityUser makeSecurityUser(User user) {
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
