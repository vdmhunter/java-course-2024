package edu.java.bot.handlers;

import edu.java.bot.exceptions.DuplicateUpdateException;
import edu.java.bot.responses.ApiErrorResponse;
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
    @ExceptionHandler(DuplicateUpdateException.class)
    public ApiErrorResponse handleDuplicateUpdateException(@NotNull DuplicateUpdateException exception) {
        String description = "Update already exists";
        String code = HttpStatus.BAD_REQUEST.getReasonPhrase();
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
