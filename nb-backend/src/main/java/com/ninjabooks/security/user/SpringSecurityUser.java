package com.ninjabooks.security.user;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SpringSecurityUser implements UserDetails
{
    private static final long serialVersionUID = -7439396305132356738L;

    private Long id;
    private String email;
    private String name;
    private String password;

    private LocalDateTime lastPasswordReset;
    private Collection<? extends GrantedAuthority> authorities;
    private Boolean accountNonExpired;
    private Boolean accountNonLocked;
    private Boolean credentialsNonExpired;
    private Boolean enabled;

    public SpringSecurityUser() {
        this(0L, null, null, null, null, null, true, true, true, true);
    }

    public SpringSecurityUser(Long id,
                              String name,
                              String password,
                              String email,
                              LocalDateTime lastPasswordReset,
                              Collection<? extends GrantedAuthority> authorities) {
        this(id, email, name, password, lastPasswordReset, authorities, true, true, true, true);
    }

    public SpringSecurityUser(Long id,
                              String email,
                              String name,
                              String password,
                              LocalDateTime lastPasswordReset,
                              Collection<? extends GrantedAuthority> authorities,
                              Boolean accountNonExpired,
                              Boolean accountNonLocked,
                              Boolean credentialsNonExpired,
                              Boolean enabled) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.lastPasswordReset = lastPasswordReset;
        this.authorities = authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLastPasswordReset() {
        return lastPasswordReset;
    }

    public void setLastPasswordReset(LocalDateTime lastPasswordReset) {
        this.lastPasswordReset = lastPasswordReset;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccountNonExpired();
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccountNonLocked();
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getCredentialsNonExpired();
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    @Override
    public boolean isEnabled() {
        return getEnabled();
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
