package edu.java.scrapper.clients.stackoverflow;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.dto.stackoverflow.Response;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        Long questionId = 1732348L;
        String responseBody = """
            {
              "items": [
                {
                  "owner": {
                    "account_id": 5713848,
                    "reputation": 26,
                    "user_id": 4514852,
                    "user_type": "registered",
                    "profile_image": "https://i.stack.imgur.com/IACe6.jpg?s=256&g=1",
                    "display_name": "Vladimir D.",
                    "link": "https://stackoverflow.com/users/4514852/vladimir-d"
                  },
                  "is_accepted": false,
                  "score": 1,
                  "last_activity_date": 1658561412,
                  "last_edit_date": 1658561412,
                  "creation_date": 1658506342,
                  "answer_id": 73083247,
                  "question_id": 73082735,
                  "content_license": "CC BY-SA 4.0"
                }
              ],
              "has_more": false,
              "quota_max": 300,
              "quota_remaining": 297
            }
            """;

        String expectedOwnerDisplayName = "Vladimir D.";
        OffsetDateTime expectedLastActivityDate = Instant.ofEpochSecond(1658561412L).atOffset(ZoneOffset.UTC);
        Long expectedAnswerId = 73083247L;
        Long expectedQuestionId = 73082735L;

        var ucb = UriComponentsBuilder
            .fromPath("/questions/{questionId}/answers")
            .queryParam("pagesize", 1)
            .queryParam("order", "desc")
            .queryParam("sort", "activity")
            .queryParam("site", "stackoverflow")
            .uriVariables(Map.of("questionId", questionId));

        wireMockServer.stubFor(get(urlEqualTo(ucb.toUriString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        // Act
        Response actualResponse = client.fetchLatestModified(questionId).get();

        // Assert
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse.owner().displayName()).isEqualTo(expectedOwnerDisplayName);
        assertThat(actualResponse.lastActivityDate()).isEqualTo(expectedLastActivityDate);
        assertThat(actualResponse.answerId()).isEqualTo(expectedAnswerId);
        assertThat(actualResponse.questionId()).isEqualTo(expectedQuestionId);
    }

    @Test
    void fetchLatestModifiedEmptyBody() {
        // Arrange
        Long questionId = 1732348L;
        String responseBody = """
            {
              "items": [],
              "has_more":,
              "quota_max":,
              "quota_remaining":
            }
            """;

        var ucb = UriComponentsBuilder
            .fromPath("/questions/{questionId}/answers")
            .queryParam("pagesize", 1)
            .queryParam("order", "desc")
            .queryParam("sort", "activity")
            .queryParam("site", "stackoverflow")
            .uriVariables(Map.of("questionId", questionId));

        wireMockServer.stubFor(get(urlEqualTo(ucb.toUriString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        // Act
        Optional<Response> actualResponse = client.fetchLatestModified(questionId);

        // Assert
        assertThat(actualResponse).isNotPresent();
    }

    @Test
    void fetchLatestModifiedInvalidBody() {
        // Arrange
        Long questionId = 1732348L;
        String responseBody = "invalid body";

        var ucb = UriComponentsBuilder
            .fromPath("/questions/{questionId}/answers")
            .queryParam("pagesize", 1)
            .queryParam("order", "desc")
            .queryParam("sort", "activity")
            .queryParam("site", "stackoverflow")
            .uriVariables(Map.of("questionId", questionId));

        wireMockServer.stubFor(get(urlEqualTo(ucb.toUriString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)
            )
        );

        // Act
        Optional<Response> actualResponse = client.fetchLatestModified(questionId);

        // Assert
        assertThat(actualResponse).isNotPresent();
    }
}
