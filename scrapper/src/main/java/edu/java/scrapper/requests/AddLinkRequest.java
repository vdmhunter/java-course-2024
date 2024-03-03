package edu.java.scrapper.requests;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record AddLinkRequest(
    @NotNull URI link
) {
}

