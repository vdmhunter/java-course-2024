package edu.java.bot.responses;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
