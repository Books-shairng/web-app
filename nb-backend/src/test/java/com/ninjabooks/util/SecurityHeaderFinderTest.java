package com.ninjabooks.util;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class SecurityHeaderFinderTest
{
    private static final String TOKEN = "asdasdjasjd1232asdlkasd.daskdad2ea'sda;slkdals";
    private static final String PATTERN = "Bearer";

    private SecurityHeaderFinder securityHeaderFinder;

    @Before
    public void setUp() throws Exception {
        this.securityHeaderFinder = new SecurityHeaderFinder();
    }

    @Test
    public void testHasSecurityPatterShouldReturnTrue() throws Exception {
        String header = PATTERN + "      " + TOKEN;
        boolean actual =  securityHeaderFinder.hasSecurityPattern(header);

        assertThat(actual).isEqualTo(true);
    }

    @Test
    public void testHasSecurityPatternWithWrongHeaderShouldThrowsException() throws Exception {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> securityHeaderFinder.hasSecurityPattern(TOKEN))
            .withNoCause()
            .withMessage("Header contains an unknow type");
    }

    @Test
    public void testExtractTokenShouldReturnToken() throws Exception {
        String header = PATTERN + "        " + TOKEN;

        String actual = securityHeaderFinder.extractToken(header);

        assertThat(actual).isEqualTo(TOKEN);
    }
}
