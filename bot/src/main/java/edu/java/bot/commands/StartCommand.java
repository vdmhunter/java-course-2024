package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.services.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StartCommand implements Command {
    private final UserService userService;
    static final String SUCCESSFUL_REGISTRATION_MSG = "You have been successfully registered!";
    static final String ALREADY_REGISTERED_MSG = "You are already registered";

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Register";
    }

    @Override
    public String handle(@NotNull Update update) {
        long chatId = update.message().chat().id();

        return generateMessage(chatId);
    }

    private String generateMessage(long chatId) {
        if (userService.register(chatId)) {
            return SUCCESSFUL_REGISTRATION_MSG;
        }

        return ALREADY_REGISTERED_MSG;
    }
}
