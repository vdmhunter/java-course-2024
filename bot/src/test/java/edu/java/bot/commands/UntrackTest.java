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
class UnTrackTest {
    static final long CHAT_ID = 1L;

    Command unTrackCommand;
    UserService userService;

    @Autowired UnTrackTest(Command unTrackCommand, UserService userService) {
        this.unTrackCommand = unTrackCommand;
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

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @DisplayName("The list of tracked links is empty")
    void emptyLinkList() {
        // Arrange
        registerUser();
        String expectedText = UnTrackCommand.EMPTY_LIST_MSG;
        SessionState expectedSessionState = SessionState.DEFAULT;

        // Act
        String actualText = unTrackCommand.handle(update);

        // Assert
        assertThat(userService.findById(CHAT_ID)).isPresent();
        assertThat(actualText).isEqualTo(expectedText);
        assertThat(userService.findById(CHAT_ID).get().getState()).isEqualTo(expectedSessionState);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @DisplayName("Change of state")
    void waitingLinkForUnTrackingSessionState() {
        // Arrange
        registerUser(List.of(URI.create("https://github.com/")));
        String expected = UnTrackCommand.UNTRACK_MSG;
        SessionState expectedSessionState = SessionState.AWAITING_UNTRACKING_LINK;

        // Act
        String actual = unTrackCommand.handle(update);

        // Assert
        assertThat(userService.findById(CHAT_ID)).isPresent();
        assertThat(actual).isEqualTo(expected);
        assertThat(userService.findById(CHAT_ID).get().getState()).isEqualTo(expectedSessionState);
    }

    @Test
    @DisplayName("The user has not been registered yet")
    void unknownUser() {
        // Arrange
        String expected = UnTrackCommand.UNKNOWN_USER_MSG;

        // Act
        String actual = unTrackCommand.handle(update);

        // Assert
        assertThat(userService.findById(CHAT_ID)).isEmpty();
        assertThat(actual).isEqualTo(expected);
    }

    private void registerUser() {
        registerUser(List.of());
    }

    private void registerUser(List<URI> links) {
        userService.addUser(new User(CHAT_ID, links, SessionState.DEFAULT));
    }
}
