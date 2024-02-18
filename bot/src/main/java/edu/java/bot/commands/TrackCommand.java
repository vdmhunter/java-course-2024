package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.SessionState;
import edu.java.bot.services.UserService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrackCommand implements Command {
    private final UserService userService;
    static final String TRACK_MSG = "Send me the tracking link";
    static final String UNKNOWN_USER_MSG = "You're not registered";

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Start tracking the link";
    }

    @Override
    public String handle(@NotNull Update update) {
        var chatId = update.message().chat().id();

        return generateMessage(chatId);
    }

    private String generateMessage(long chatId) {
        if (userService.changeSessionState(chatId, SessionState.AWAITING_TRACKING_LINK)) {
            return TRACK_MSG;
        }

        return UNKNOWN_USER_MSG;
    }
}
