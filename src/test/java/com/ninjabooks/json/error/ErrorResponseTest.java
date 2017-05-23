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
    private  ErrorResponse errorResponse;


    @Test
    public void testShouldReturnCorrectData() throws Exception {
        Exception exception = new Exception("Testing");
        HttpStatus httpStatus = HttpStatus.ACCEPTED;
        String request = "/api/users";

        errorResponse = new ErrorResponse(httpStatus, exception, request);

        assertSoftly(softly -> {
            assertThat(errorResponse.getMessage()).isEqualTo(exception.getMessage());
            assertThat(errorResponse.getRequest()).isEqualTo(request);
            assertThat(errorResponse.getStatus()).isEqualTo(httpStatus.value());
        });
    }
}
