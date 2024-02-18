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
class StartTest {
    static final long CHAT_ID = 1L;

    Command startCommand;
    UserService userService;

    @Autowired StartTest(Command startCommand, UserService userService) {
        this.startCommand = startCommand;
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
    @DisplayName("User registration, then re-registration")
    void registerUserTwice() {
        // Arrange
        String expected1 = StartCommand.SUCCESSFUL_REGISTRATION_MSG;
        String expected2 = StartCommand.ALREADY_REGISTERED_MSG;

        // Act
        String actual1 = startCommand.handle(update);
        String actual2 = startCommand.handle(update);

        // Assert
        assertThat(userService.findById(CHAT_ID)).isPresent();
        assertThat(actual1).isEqualTo(expected1);
        assertThat(actual2).isEqualTo(expected2);
    }
}
