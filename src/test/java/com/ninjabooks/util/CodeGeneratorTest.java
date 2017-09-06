package com.ninjabooks.util;

import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class CodeGeneratorTest
{
    private static final int EXPECTED_SIZE = 10;

    private CodeGenerator sut;

    @Before
    public void setUp() throws Exception {
        this.sut = new CodeGenerator();
    }

    @Test
    public void testLengthOfGeneratedCodeEqualsDesiredNumber() throws Exception {
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
    public void testTwoGeneratedCodesShouldBeUnique() throws Exception {
        String firstCode = sut.generateCode();
        String secondCode = sut.generateCode();

        assertThat(firstCode).isNotEqualTo(secondCode);
    }
}
