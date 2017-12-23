package com.ninjabooks.security.service.auth;

import com.ninjabooks.json.authentication.AuthenticationRequest;
import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.security.utils.TokenUtils;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.utils.TestDevice;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class AuthenticationServiceImplTest
{
    private static final String SECURITY_PATTERN = "Bearer ";
    private static final String SECRET = "aaaaazzzzzxxxxccccvv";
    private static final String OLD_TOKEN =
        "Bearer eyJhbGciOiJIUzUxMiJ9" +
            ".eyJzdWIiOiJqb2huLmRlZUBleG1hcGxlLmNvbSIsImF1ZGllbmNlIjoidW5rbm93biIsImNyZWF0ZWQiOjE1MTM3" +
            "MTM2MjQwODcsImV4cCI6MTUxNDMxODQyNH0" +
            ".rIb7FVGxvbfzK_ljh6P3pylxThy6abn_OW0jfLtXvfWE5CTSedwONR0pE7JyS7HassRD6zOI4UpgF0jSQLHTPg";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private UserDetailsService userDetailsServiceMock;

    private TokenUtils tokenUtils = new TokenUtils(SECRET);
    private AuthenticationService sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new AuthenticationServiceImpl(authenticationManagerMock, userDetailsServiceMock, tokenUtils);
    }

    @Test
    public void testAuthUserShouldReturnUser() throws Exception {
        when(userDetailsServiceMock.loadUserByUsername(DomainTestConstants.EMAIL)).thenReturn(initSpringUser());
        UserDetails actual =
            sut.authUser(createAuthRequest());

        assertThat(actual).extracting("username").containsExactly(DomainTestConstants.EMAIL);
        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(anyString());
    }

    @Test
    public void testAuthUserShouldThrowsExceptionWhenUserDataNotValid() throws Exception {
        doThrow(BadCredentialsException.class)
            .when(userDetailsServiceMock).loadUserByUsername(DomainTestConstants.EMAIL);

        assertThatExceptionOfType(BadCredentialsException.class)
            .isThrownBy(() -> sut.authUser(createAuthRequest()))
            .withNoCause();

        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(anyString());
    }

    @Test
    public void testRefreshTokenShouldReturnNewToken() throws Exception {
        when(userDetailsServiceMock.loadUserByUsername(DomainTestConstants.EMAIL)).thenReturn(initSpringUser());
        String token = generateToken();
        Optional<String> actual = sut.refreshToken(token);

        assertSoftly(softly -> {
            assertThat(actual).isNotEmpty();
            assertThat(actual).isNotEqualTo(token);
        });
        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(anyString());
    }

    @Test
    public void testRefreshTokenShoulReturnEmptyToken() throws Exception {
        when(userDetailsServiceMock.loadUserByUsername(DomainTestConstants.EMAIL)).thenReturn(initSpringUser());
        Optional<String> actual = sut.refreshToken(OLD_TOKEN);

        assertThat(actual).isEmpty();
        verify(userDetailsServiceMock, atLeastOnce()).loadUserByUsername(anyString());
    }

    @Test
    public void testGetAuthUserShouldReturnExpectedSpringUser() throws Exception {
        when(userDetailsServiceMock.loadUserByUsername(DomainTestConstants.EMAIL)).thenReturn(initSpringUser());
        SpringSecurityUser actual = sut.getAuthUser(generateToken());

        assertThat(actual).extracting("username").containsExactly(DomainTestConstants.EMAIL);
    }

    private String generateToken() {
        String token = tokenUtils.generateToken(initSpringUser(), TestDevice.createDevice());
        return SECURITY_PATTERN + token;
    }

    private AuthenticationRequest createAuthRequest() {
        return new AuthenticationRequest(DomainTestConstants.EMAIL, DomainTestConstants.PASSWORD);
    }

    private SpringSecurityUser initSpringUser() {
        SpringSecurityUser user = new SpringSecurityUser();
        user.setEmail(DomainTestConstants.EMAIL);
        user.setLastPasswordReset(LocalDateTime.now().minusSeconds(5));
        return user;
    }
}
