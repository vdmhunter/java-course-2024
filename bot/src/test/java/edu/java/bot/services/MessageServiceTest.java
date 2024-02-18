package edu.java.bot.services;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.models.SessionState;
import edu.java.bot.models.User;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {BotApplication.class})
class MessageServiceTest {
    @MockBean
    Update update;
    UserService userService;
    MessageService messageService;

    @Autowired MessageServiceTest(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    private static final long CHAT_ID = 1L;

    private void setUpMock(String text) {
        Message messageMock = mock(Message.class);
        Chat chatMock = mock(Chat.class);

        when(update.message()).thenReturn(messageMock);
        when(messageMock.chat()).thenReturn(chatMock);

        when(chatMock.id()).thenReturn(CHAT_ID);
        when(update.message().chat().id()).thenReturn(CHAT_ID);

        when(messageMock.text()).thenReturn(text);
        when(update.message().text()).thenReturn(text);
    }

    @AfterEach
    void clearDatabase() {
        userService.clear();
    }

    @ParameterizedTest
    @MethodSource("notRegisteredUserCommands")
    @DisplayName("User not registered, attempt to enter incorrect commands")
    void unknownUser(String text, String excepted) {
        // Arrange
        setUpMock(text);

        // Act
        String actual = messageService.createResponseText(update);

        // Assert
        assertThat(actual).isEqualTo(excepted);
    }

    private @NotNull Stream<Arguments> registeredUserCommands() {
        return Stream.of(
            Arguments.of(List.of(), SessionState.DEFAULT, "/bla", MessageService.INVALID_COMMAND_MSG),
            Arguments.of(
                List.of(),
                SessionState.AWAITING_TRACKING_LINK,
                "abc 12345",
                MessageService.INVALID_LINK_MSG
            ),
            Arguments.of(
                List.of(),
                SessionState.AWAITING_TRACKING_LINK,
                "https://habr.com/",
                MessageService.NOT_SUPPORTED_LINK_MSG
            ),
            Arguments.of(
                List.of(),
                SessionState.AWAITING_TRACKING_LINK,
                "https://github.com/",
                MessageService.SUCCESSFUL_TRACKING_MSG
            ),
            Arguments.of(
                List.of(URI.create("https://github.com/")),
                SessionState.AWAITING_TRACKING_LINK,
                "https://github.com/",
                MessageService.DUPLICATE_TRACKING_MSG
            ),
            Arguments.of(
                List.of(URI.create("https://github.com/")),
                SessionState.AWAITING_UNTRACKING_LINK,
                "https://stackoverflow.com/",
                MessageService.ABSENT_UNTRACKING_MSG
            ),
            Arguments.of(
                List.of(URI.create("https://github.com/")),
                SessionState.AWAITING_UNTRACKING_LINK,
                "https://github.com/",
                MessageService.SUCCESSFUL_UNTRACKING_MSG
            )
        );
    }

    @ParameterizedTest
    @MethodSource("registeredUserCommands")
    @DisplayName("User registered, attempting to enter incorrect commands")
    void registeredUser(List<URI> links, SessionState sessionState, String text, String excepted) {
        // Arrange
        setUpMock(text);
        registerUser(links, sessionState);

        // Act
        String actual = messageService.createResponseText(update);

        // Assert
        assertThat(actual).isEqualTo(excepted);
    }

    private void registerUser(List<URI> links, SessionState sessionState) {
        userService.addUser(new User(CHAT_ID, links, sessionState));
    }

    private @NotNull Stream<Arguments> notRegisteredUserCommands() {
        return Stream.of(
            Arguments.of("/bla", MessageService.UNKNOWN_USER_MSG),
            Arguments.of("set of words", MessageService.UNKNOWN_USER_MSG)
        );
    }
}
