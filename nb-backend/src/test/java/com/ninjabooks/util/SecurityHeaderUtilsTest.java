package com.ninjabooks.util;

import org.junit.Before;
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
    private static final String PATTERN = "Bearer";
    private static final String REQUEST = PATTERN + " " + TOKEN;

    private SecurityHeaderUtils sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new SecurityHeaderUtils();
    }

    @Test
    public void testHasSecurityPatterShouldReturnTrue() throws Exception {
        boolean actual = sut.hasSecurityPattern(REQUEST);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void testHasSecurityPatternWithWrongHeaderShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> sut.hasSecurityPattern(TOKEN))
            .withNoCause()
            .withMessage("Header contains an unknow type");
    }

    @Test
    public void testExtractTokenShouldReturnToken() throws Exception {
        String actual = sut.extractToken(REQUEST);

        assertThat(actual).isEqualTo(TOKEN);
    }

    @Test
    public void obtainTokenFromRequestShouldExtractToken() throws Exception {
        String actual = sut.obtainTokenFromRequest(REQUEST);

        assertThat(actual).isEqualTo(TOKEN);
    }
}
