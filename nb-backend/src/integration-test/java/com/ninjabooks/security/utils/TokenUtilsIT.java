package com.ninjabooks.security.utils;

import com.ninjabooks.config.IntegrationTest;
import com.ninjabooks.util.constants.DomainTestConstants;
import com.ninjabooks.utils.TestDevice;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@IntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(value = "classpath:it_import.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TokenUtilsIT
{
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate EXPECTED_EXP_DATE = TODAY.plusDays(7);
    private static final String EXPECTED_AUDIENCE = "unknown";
    private static final String RANDOM_TOKEN =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9" +
            ".TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenUtils sut;

    @Test
    public void testGenerateTokenShouldSucceed() throws Exception {
        String actual = sut.generateToken(obtainUserDetails(), TestDevice.createDevice());

        assertThat(actual).isNotEmpty().containsPattern("\\.+{2}");
    }

    @Test
    public void testGetUsernameShouldReturnExpectedUsername() throws Exception {
        String actual = sut.getUsernameFromToken(generateToken());

        assertThat(actual).isEqualTo(DomainTestConstants.EMAIL);
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
        Boolean actual = sut.isValid(generateToken(), obtainUserDetails());

        assertThat(actual).isTrue();
    }

    @Test
    public void testIsValidateTokenWithWrongTokenShouldFailedAndReturnFalse() throws Exception {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> sut.isValid(RANDOM_TOKEN, obtainUserDetails()))
            .withNoCause();
    }

    private UserDetails obtainUserDetails() {
        return userDetailsService.loadUserByUsername(DomainTestConstants.EMAIL);
    }

    private String generateToken() {
        return sut.generateToken(obtainUserDetails(), TestDevice.createDevice());
    }
}
