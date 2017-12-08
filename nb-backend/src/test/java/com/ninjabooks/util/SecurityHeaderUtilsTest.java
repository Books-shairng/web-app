package com.ninjabooks.util;

import com.ninjabooks.security.utils.SecurityHeaderUtils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SecurityHeaderUtilsTest
{
    private static final String TOKEN = "asdasdjasjd1232asdlkasd.daskdad2ea'sda;slkdals";
    private static final String SECURITY_PATTERN = "Bearer";
    private static final String REQUEST = SECURITY_PATTERN + " " + TOKEN;

    @Test
    public void testExtractTokennWithWrongHeaderShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> SecurityHeaderUtils.extractTokenFromHeader(TOKEN))
            .withNoCause()
            .withMessage("Header does not contains proper token");
    }

    @Test
    public void testExtractTokenWithoutTokenShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> SecurityHeaderUtils.extractTokenFromHeader(SECURITY_PATTERN))
            .withNoCause()
            .withMessage("Header does not contains proper token");
    }

    @Test
    public void testExtractTokenShouldReturnToken() throws Exception {
        String actual = SecurityHeaderUtils.extractTokenFromHeader(REQUEST);

        assertThat(actual).isEqualTo(TOKEN);
    }

    @Test
    public void testExtractTokenWithOnlySecurityPatternPlusSpaceShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> SecurityHeaderUtils.extractTokenFromHeader(SECURITY_PATTERN + " "))
            .withNoCause()
            .withMessage("Header does not contains proper token");
    }
}
