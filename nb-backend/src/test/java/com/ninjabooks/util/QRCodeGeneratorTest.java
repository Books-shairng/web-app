package com.ninjabooks.util;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class QRCodeGeneratorTest
{
    private static final int EXPECTED_SIZE = 10;
    private static final String SLASH_SIGN = "\\";

    private QRCodeGenerator sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new QRCodeGenerator();
    }

    @Test
    public void testLengthOfGeneratedCodeShouldEqualsExpectedSize() throws Exception {
        String actual = sut.generateCode();

        assertThat(actual).hasSize(EXPECTED_SIZE);
    }

    @Test
    public void testGeneratedCodeShouldContainsOnlyDesiredCharacters() throws Exception {
        String actual = sut.generateCode();
        String regex = "[\\d\\S\\w]+";
        Pattern pattern = Pattern.compile(regex);

        assertThat(actual).containsPattern(pattern);
    }

    @Test
    public void testGenerateCodeShouldNotContainsSlashSign() throws Exception {
        String actual = sut.generateCode();

        assertThat(actual).doesNotContain(SLASH_SIGN);
    }

    @Test
    public void testTwoGeneratedCodesShouldBeUnique() throws Exception {
        String firstCode = sut.generateCode();
        String secondCode = sut.generateCode();

        assertThat(firstCode).isNotEqualTo(secondCode);
    }
}
