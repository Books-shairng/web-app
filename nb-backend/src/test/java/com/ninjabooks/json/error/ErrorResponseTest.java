package com.ninjabooks.json.error;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public class ErrorResponseTest
{
    private ErrorResponse sut;

    @Test
    public void testShouldReturnCorrectData() throws Exception {
        Exception exception = new Exception("Testing");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        String request = "/api/user";

        sut = new ErrorResponse(httpStatus, exception, request);

        assertSoftly(softly -> {
            assertThat(sut.getMessage()).isEqualTo(exception.getMessage());
            assertThat(sut.getRequest()).isEqualTo(request);
            assertThat(sut.getStatus()).isEqualTo(httpStatus.value());
        });
    }
}
