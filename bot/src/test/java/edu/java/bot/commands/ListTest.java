package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.models.SessionState;
import edu.java.bot.models.User;
import edu.java.bot.services.UserService;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {BotApplication.class})
class ListTest {
    static final long CHAT_ID = 1L;

    Command listCommand;
    UserService userService;

    @Autowired ListTest(Command listCommand, UserService userService) {
        this.listCommand = listCommand;
        this.userService = userService;
    }

    @MockBean
    Update update;

    @BeforeEach
    void setUpMock() {
        Message messageMock = mock(Message.class);
        Chat chatMock = mock(Chat.class);

        when(update.message()).thenReturn(messageMock);
        when(messageMock.chat()).thenReturn(chatMock);

        when(chatMock.id()).thenReturn(CHAT_ID);
        when(update.message().chat().id()).thenReturn(CHAT_ID);
    }

    @AfterEach
    void clearDatabase() {
        userService.clear();
    }

    @Test
    @DisplayName("The list of tracked links is empty")
    void emptyLinkList() {
        // Arrange
        registerUser();
        String expected = ListCommand.EMPTY_LIST_MSG;

        // Act
        String actual = listCommand.handle(update);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Non-empty list of tracked references")
    void nonEmptyLinkList() {
        // Arrange
        String link = "https://github.com/";
        registerUser(List.of(URI.create(link)));
        String excepted = ListCommand.LIST_MSG + link + System.lineSeparator();

        // Act
        String actual = listCommand.handle(update);

        // Assert
        assertThat(actual).isEqualTo(excepted);
    }

    @Test
    @DisplayName("The user has not been registered yet")
    void unknownUser() {
        // Arrange
        String excepted = ListCommand.UNKNOWN_USER_MSG;

        // Act
        String actual = listCommand.handle(update);

        // Assert
        assertThat(actual).isEqualTo(excepted);
    }

    private void registerUser() {
        registerUser(List.of());
    }

    private void registerUser(List<URI> links) {
        userService.addUser(new User(CHAT_ID, links, SessionState.DEFAULT));
    }
}
