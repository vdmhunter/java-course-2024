package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.BotApplication;
import edu.java.bot.services.UserService;
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
class HelpTelegramCommandTest {
    static final long CHAT_ID = 1L;
    static final String LINE_SEPARATOR = System.lineSeparator();

    TelegramCommand helpTelegramCommand;
    UserService userService;

    @Autowired HelpTelegramCommandTest(TelegramCommand helpTelegramCommand, UserService userService) {
        this.helpTelegramCommand = helpTelegramCommand;
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

    @SuppressWarnings("SpellCheckingInspection")
    @Test
    @DisplayName("Help display")
    void help() {
        // Arrange
        String expected = HelpTelegramCommand.HELP_MSG
            + "/start — Register" + LINE_SEPARATOR
            + "/track — Start tracking the link" + LINE_SEPARATOR
            + "/untrack — Stop tracking the link" + LINE_SEPARATOR
            + "/list — Show list of tracked links" + LINE_SEPARATOR;

        // Act
        String actual = helpTelegramCommand.handle(update);

        // Assert
        assertThat(actual).isEqualTo(expected);
    }
}
