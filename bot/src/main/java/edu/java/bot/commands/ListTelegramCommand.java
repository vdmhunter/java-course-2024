package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.models.User;
import edu.java.bot.services.UserService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListTelegramCommand implements TelegramCommand {
    private final UserService userService;
    static final String LINE_SEPARATOR = System.lineSeparator();
    static final String EMPTY_LIST_MSG = "The list of tracked links is empty";
    static final String UNKNOWN_USER_MSG = "You're not registered";
    static final String LIST_MSG = "The following links are tracked by the bot:" + LINE_SEPARATOR;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Show list of tracked links";
    }

    @Override
    public String handle(@NotNull Update update) {
        Long chatId = update.message().chat().id();

        return generateMessage(chatId);
    }

    private String generateMessage(long chatId) {
        Optional<User> initiator = userService.findById(chatId);

        if (initiator.isPresent()) {
            List<URI> links = initiator.get().getLinks();

            if (links.isEmpty()) {
                return EMPTY_LIST_MSG;
            }

            return generateTextList(links);
        }

        return UNKNOWN_USER_MSG;
    }

    private @NotNull String generateTextList(@NotNull List<URI> links) {
        var text = new StringBuilder();
        text.append(LIST_MSG);

        for (URI uri : links) {
            text.append(uri.toString()).append(System.lineSeparator());
        }

        return text.toString();
    }
}
