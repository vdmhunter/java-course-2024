package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.SessionState;
import edu.java.bot.models.User;
import edu.java.bot.services.UserService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpellCheckingInspection")
@Component
@AllArgsConstructor
public class UnTrackTelegramCommand implements TelegramCommand {
    private final UserService userService;
    static final String UNTRACK_MSG = "Send a link to stop tracking it";
    static final String UNKNOWN_USER_MSG = "You're not registered in bot";
    static final String EMPTY_LIST_MSG = "The list of tracked links is empty";

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stop tracking the link";
    }

    @Override
    public String handle(@NotNull Update update) {
        var chatId = update.message().chat().id();

        return generateMessage(chatId);
    }

    private String generateMessage(long chatId) {
        Optional<User> initiator = userService.findById(chatId);

        if (initiator.isPresent()) {
            List<URI> links = initiator.get().getLinks();

            if (links.isEmpty()) {
                return EMPTY_LIST_MSG;
            }
        }

        if (userService.changeSessionState(chatId, SessionState.AWAITING_UNTRACKING_LINK)) {
            return UNTRACK_MSG;
        }

        return UNKNOWN_USER_MSG;
    }
}
