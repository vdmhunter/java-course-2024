package edu.java.bot.responses;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
