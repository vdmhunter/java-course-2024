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
class TrackTelegramCommandTest {
    static final long CHAT_ID = 1L;

    TelegramCommand trackTelegramCommand;
    UserService userService;

    @Autowired TrackTelegramCommandTest(TelegramCommand trackTelegramCommand, UserService userService) {
        this.trackTelegramCommand = trackTelegramCommand;
        this.userService = userService;
    }

    @MockBean
    Update update;

    @BeforeEach
    void setUp() {
        Message messageMock = mock(Message.class);
        Chat chatMock = mock(Chat.class);

        when(update.message()).thenReturn(messageMock);
        when(messageMock.chat()).thenReturn(chatMock);

        when(chatMock.id()).thenReturn(CHAT_ID);
        when(update.message().chat().id()).thenReturn(CHAT_ID);
    }

    @AfterEach
    void tearDown() {
        userService.clear();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    @DisplayName("Change of state")
    void waitingLinkForTrackingSessionState() {
        // Arrange
        registerUser();
        String expected = TrackTelegramCommand.TRACK_MSG;
        SessionState expectedSessionState = SessionState.AWAITING_TRACKING_LINK;

        // Act
        String actual = trackTelegramCommand.handle(update);

        // Assert
        assertThat(userService.findById(CHAT_ID)).isPresent();
        assertThat(actual).isEqualTo(expected);
        assertThat(userService.findById(CHAT_ID).get().getState()).isEqualTo(expectedSessionState);
    }

    @Test
    @DisplayName("The user has not been registered yet")
    void unknownUser() {
        // Arrange
        String expected = TrackTelegramCommand.UNKNOWN_USER_MSG;

        // Act
        String actual = trackTelegramCommand.handle(update);

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
