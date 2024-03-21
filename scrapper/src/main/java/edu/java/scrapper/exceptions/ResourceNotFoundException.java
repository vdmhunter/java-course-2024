package edu.java.scrapper.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private final String description;

    public ResourceNotFoundException(String message, String description) {
        super(message);
        this.description = description;
    }
}

