package com.ninjabooks.security.utils;

import com.ninjabooks.security.user.SpringSecurityUser;
import com.ninjabooks.utils.TestDevice;

import static com.ninjabooks.util.constants.DomainTestConstants.EMAIL;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class TokenUtilsTest
{
    private static final String SECRET_HASH_VALUE = "secret12345";
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate EXPECTED_EXP_DATE = TODAY.plusDays(7);
    private static final String EXPECTED_AUDIENCE = "unknown";
    private static final String RANDOM_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9" +
            ".TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule().silent();

    @Mock
    private SpringSecurityUser springSecurityUserMock;

    private TokenUtils sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new TokenUtils(SECRET_HASH_VALUE);
    }

    @Test
    public void testGenerateTokenShouldSucceed() throws Exception {
        String actual = sut.generateToken(springSecurityUserMock, TestDevice.createDevice());

        assertThat(actual).isNotEmpty().containsPattern("\\.+{2}");
    }

    @Test
    public void testGetUsernameShouldReturnExpectedUsername() throws Exception {
        when(springSecurityUserMock.getUsername()).thenReturn(EMAIL);
        String actual = sut.getUsernameFromToken(generateToken());

        assertThat(actual).isEqualTo(EMAIL);
        verify(springSecurityUserMock, atLeastOnce()).getUsername();
    }

    @Test
    public void testGetUsernameFromWrongTokenShouldReturnNull() throws Exception {
        String actual = sut.getUsernameFromToken(RANDOM_TOKEN);

        assertThat(actual).isNull();
    }

    @Test
    public void testGetCreatedDateShouldRetunExpectedDate() throws Exception {
        LocalDateTime actual = sut.getCreatedDateFromToken(generateToken());

        assertThat(actual.toLocalDate()).isEqualTo(TODAY);
    }

    @Test
    public void testGetCreatedDateFromWrongTokenShouldRetunNull() throws Exception {
        LocalDateTime actual = sut.getCreatedDateFromToken(RANDOM_TOKEN);

        assertThat(actual).isNull();
    }

    @Test
    public void testGetExpirationDateShouldReturnExptectedDate() throws Exception {
        LocalDateTime actual = sut.getExpirationDateFromToken(generateToken());

        assertThat(actual.toLocalDate()).isEqualTo(EXPECTED_EXP_DATE);
    }

    @Test
    public void testGetExpirationDateFromWrongTokenShouldReturnNull() throws Exception {
        LocalDateTime actual = sut.getExpirationDateFromToken(RANDOM_TOKEN);

        assertThat(actual).isNull();
    }

    @Test
    public void testGetAudienceShouldReturnExpectedAudience() throws Exception {
        String actual = sut.getAudienceFromToken(generateToken());

        assertThat(actual).isEqualTo(EXPECTED_AUDIENCE);
    }

    @Test
    public void testGetAudienceFromWrongTokenShouldReturnNull() throws Exception {
        String actual = sut.getAudienceFromToken(RANDOM_TOKEN);

        assertThat(actual).isNull();
    }

    @Test
    public void testCanTokenRefreshShouldReturnTrue() throws Exception {
        Boolean actual = sut.canTokenBeRefreshed(generateToken(), TODAY.atStartOfDay());

        assertThat(actual).isTrue();
    }

    @Test
    public void testRefreshTokenShouldSucceedAndReturnNewToken() throws Exception {
        String tokenToRefresh = generateToken();
        String actual = sut.refreshToken(tokenToRefresh);

        assertThat(actual).isNotEqualTo(tokenToRefresh);
    }

    @Test
    public void testRefreshTokenShouldReturnNullWhenTokenIsWrong() throws Exception {
        String actual = sut.refreshToken(RANDOM_TOKEN);

        assertThat(actual).isNull();
    }

    @Test
    public void testIsValidateTokenShouldSuceedAndReturnTrue() throws Exception {
        when(springSecurityUserMock.getUsername()).thenReturn(EMAIL);
        Boolean actual = sut.isValid(generateToken(), springSecurityUserMock);

        assertThat(actual).isTrue();
        verify(springSecurityUserMock, atLeastOnce()).getUsername();
    }

    @Test
    public void testIsValidateTokenWithWrongTokenShouldFailedAndReturnFalse() throws Exception {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> sut.isValid(RANDOM_TOKEN, springSecurityUserMock))
            .withNoCause();
    }

    private String generateToken() {
        return sut.generateToken(springSecurityUserMock, TestDevice.createDevice());
    }
}
