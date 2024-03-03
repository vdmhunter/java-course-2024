package edu.java.scrapper.handlers;

import edu.java.scrapper.exceptions.BadRequestException;
import edu.java.scrapper.exceptions.ResourceNotFoundException;
import edu.java.scrapper.responses.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiResponseExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiErrorResponse handleBadRequestException(@NotNull BadRequestException exception) {
        return createErrorResponse(exception, HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiErrorResponse handleResourceNotFoundException(@NotNull ResourceNotFoundException exception) {
        return createErrorResponse(exception, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    private @NotNull ApiErrorResponse createErrorResponse(@NotNull RuntimeException exception, String code) {
        String description = exception.getMessage();
        String exceptionName = exception.getClass().getSimpleName();
        String exceptionMessage = exception.getMessage();
        List<String> stackTrace = Arrays.stream(exception.getStackTrace())
            .map(Object::toString)
            .toList();

        return new ApiErrorResponse(
            description,
            code,
            exceptionName,
            exceptionMessage,
            stackTrace
        );
    }
}
