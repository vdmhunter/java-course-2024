package edu.java.bot.exceptions;

import edu.java.bot.responses.ApiErrorResponse;
import java.io.Serial;
import lombok.Getter;

@Getter
public class ApiResponseException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 7156077805866692314L;

    private final ApiErrorResponse errorResponse;

    public ApiResponseException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
