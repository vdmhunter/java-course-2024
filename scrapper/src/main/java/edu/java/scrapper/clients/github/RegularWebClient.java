package edu.java.scrapper.clients.github;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.scrapper.dto.github.Response;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;


public class RegularWebClient implements Client {
    @Value("${api.github.baseUrl}")
    private String baseUrl;
    private final WebClient webClient;

    public RegularWebClient() {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public RegularWebClient(@NotNull String baseUrl) {
        String finalBaseUrl = baseUrl;

        if (baseUrl.isEmpty()) {
            finalBaseUrl = this.baseUrl;
        }

        this.webClient = WebClient.builder().baseUrl(finalBaseUrl).build();
    }

    @Override
    public Optional<Response> fetchLatestModified(String authorName, String repositoryName) {
        String requestUrl = String.format("networks/%s/%s/events", authorName, repositoryName);

        return Optional.ofNullable(webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(requestUrl)
                .queryParam("per_page", 1)
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .mapNotNull(this::parse)
            .block()
        );
    }

    private @Nullable Response parse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            List<Response> responses = objectMapper.readValue(json, new TypeReference<>(){});
            return responses.get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
