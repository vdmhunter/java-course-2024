package edu.java.scrapper.clients.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.dto.github.Response;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("SpellCheckingInspection")
class RegularWebClientTest {
    private WireMockServer wireMockServer;
    private Client client;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
        String baseUrl = "http://localhost:" + wireMockServer.port();
        client = new RegularWebClient(baseUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void fetchLatestModified() {
        // Arrange
        String authorName = "vdmhunter";
        String repositoryName = "java-course-2023";
        String responseBody =
            """
            [
              {
                "id": "34480269151",
                "type": "WatchEvent",
                "actor": {
                  "id": 96127451,
                  "login": "phong28zk",
                  "display_login": "phong28zk",
                  "gravatar_id": "",
                  "url": "https://api.github.com/users/phong28zk",
                  "avatar_url": "https://avatars.githubusercontent.com/u/96127451?"
                },
                "repo": {
                  "id": 699550766,
                  "name": "vdmhunter/java-course-2023",
                  "url": "https://api.github.com/repos/vdmhunter/java-course-2023"
                },
                "payload": {
                  "action": "started"
                },
                "public": true,
                "created_at": "2023-12-30T09:53:02Z"
              }
            ]
            """;

        Long expectedId = 34480269151L;
        String expectedType = "WatchEvent";
        String expectedActorDisplayLogin = "phong28zk";
        String expectedRepoName = "vdmhunter/java-course-2023";
        OffsetDateTime expectedCreatedAt = OffsetDateTime.parse("2023-12-30T09:53:02Z");

        var ucb = UriComponentsBuilder
            .fromPath("/networks/{authorName}/{repositoryName}/events")
            .queryParam("per_page", 1)
            .uriVariables(Map.of("authorName", authorName, "repositoryName", repositoryName));

        wireMockServer.stubFor(get(urlEqualTo(ucb.toUriString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        // Act
        Response actualResponse = client.fetchLatestModified(authorName, repositoryName).get();

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.id()).isEqualTo(expectedId);
        assertThat(actualResponse.type()).isEqualTo(expectedType);
        assertThat(actualResponse.actor().displayLogin()).isEqualTo(expectedActorDisplayLogin);
        assertThat(actualResponse.repo().name()).isEqualTo(expectedRepoName);
        assertThat(actualResponse.createdAt()).isEqualTo(expectedCreatedAt);
    }

    @Test
    void fetchLatestModifiedEmptyBody() {
        // Arrange
        String authorName = "vdmhunter";
        String repositoryName = "java-course-2023";
        String responseBody = "[]";

        var ucb = UriComponentsBuilder
            .fromPath("/networks/{authorName}/{repositoryName}/events")
            .queryParam("per_page", 1)
            .uriVariables(Map.of("authorName", authorName, "repositoryName", repositoryName));

        wireMockServer.stubFor(get(urlEqualTo(ucb.toUriString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        // Act
        Optional<Response> actualResponse = client.fetchLatestModified(authorName, repositoryName);

        // Assert
        assertThat(actualResponse).isNotPresent();
    }

    @Test
    void fetchLatestModifiedInvalidBody() {
        // Arrange
        String authorName = "vdmhunter";
        String repositoryName = "java-course-2023";
        String responseBody = "invalid body";

        var ucb = UriComponentsBuilder
            .fromPath("/networks/{authorName}/{repositoryName}/events")
            .queryParam("per_page", 1)
            .uriVariables(Map.of("authorName", authorName, "repositoryName", repositoryName));

        wireMockServer.stubFor(get(urlEqualTo(ucb.toUriString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        // Act
        Optional<Response> actualResponse = client.fetchLatestModified(authorName, repositoryName);

        // Assert
        assertThat(actualResponse).isNotPresent();
    }
}
