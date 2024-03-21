package edu.java.scrapper.responses;

import java.util.List;

public record ListLinksResponse(
    List<LinkResponse> links,
    int size
) {
}
