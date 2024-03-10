package edu.java.scrapper;

import edu.java.scrapper.configs.PostgresConfig;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Homework 4
 */
@SpringJUnitConfig(PostgresConfig.class)
class DatabaseIntegrationTest extends IntegrationTest {
    @Autowired
    private DataSource source;

    @Test
    @DisplayName("Test chat-link relation database operations")
    void databaseIntegration_TestChatLinkRelationDatabaseOperations() {
        // Assert
        var template = new JdbcTemplate(source);

        long expectedChatId = 1L;
        long expectedLinkId = 5L;
        String expectedUrl = "https://edu.tinkoff.ru/";

        // Act
        template.update("insert into link_tracking_db.chat (id) values (?)",
            expectedChatId
        );

        template.update("insert into link_tracking_db.link (id, url) values (?, ?)",
            expectedLinkId,
            expectedUrl
        );

        template.update(
            "insert into link_tracking_db.chat_link_relations (chat_id, link_id) values (?, ?)",
            expectedChatId,
            expectedLinkId
        );

        Long actualChatId = template.queryForObject(
            "select id from link_tracking_db.chat where id = ?",
            Long.class,
            expectedChatId
        );

        String actualUrl = template.queryForObject(
            "select url from link_tracking_db.link where id = ?",
            String.class,
            expectedLinkId
        );

        Long actualLinkId = template.queryForObject(
            "select link_id from link_tracking_db.chat_link_relations where chat_id = ?",
            Long.class,
            expectedChatId
        );

        // Assert
        assertThat(actualChatId).isEqualTo(expectedChatId);
        assertThat(actualUrl).isEqualTo(expectedUrl);
        assertThat(actualLinkId).isEqualTo(expectedLinkId);
    }
}
