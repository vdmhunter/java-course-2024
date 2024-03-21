package edu.java.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException {
    private final String description;

    public BadRequestException(String message, String description) {
        super(message);
        this.description = description;
    }
}
