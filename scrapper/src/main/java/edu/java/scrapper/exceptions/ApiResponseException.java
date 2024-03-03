package edu.java.scrapper.exceptions;

import edu.java.scrapper.responses.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ApiResponseException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ApiResponseException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
