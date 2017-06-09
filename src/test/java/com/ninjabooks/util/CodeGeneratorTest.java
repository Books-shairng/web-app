package com.ninjabooks.util;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
@ActiveProfiles(value = "test")
public class CodeGeneratorTest
{
    private final CodeGenerator codeGenerator = new CodeGenerator();

    @Test
    public void testLengthOfGeneratedCodeEqualsDesiredNumber() throws Exception {
        String actual = codeGenerator.generateCode();

        assertThat(actual).hasSize(5);
    }

    @Test
    public void testGeneratedCodeShouldContainsOnlyDesiredCharacters() throws Exception {
        String actual = codeGenerator.generateCode();

        String regex = "[\\d\\S\\w]+";
        Pattern pattern = Pattern.compile(regex);
        assertThat(actual).containsPattern(pattern);
    }

    @Test
    public void testTwoGeneratedCodesAreUnique() throws Exception {
        String firstCode = codeGenerator.generateCode();
        String secondCode = codeGenerator.generateCode();

        assertThat(firstCode).isNotEqualTo(secondCode);
    }
}
