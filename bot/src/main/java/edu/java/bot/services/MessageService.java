package edu.java.bot.services;

import com.pengrad.telegrambot.model.Update;
import edu.java.bot.commands.TelegramCommand;
import edu.java.bot.handlers.TelegramCommandHandler;
import edu.java.bot.models.User;
import edu.java.bot.validators.LinkValidator;
import java.net.URI;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@SuppressWarnings("SpellCheckingInspection")
@Service
@AllArgsConstructor
public class MessageService {
    private final UserService userService;
    private final LinkValidator linkValidator;
    private final TelegramCommandHandler commandHandler;
    static final String UNKNOWN_USER_MSG = "You're not registered in bot";
    static final String INVALID_LINK_MSG = "You have entered an incorrect link";
    static final String INVALID_COMMAND_MSG = "You have entered an incorrect command";
    static final String SUCCESSFUL_TRACKING_MSG = "This link is now being tracked!";
    static final String DUPLICATE_TRACKING_MSG = "This link is already tracking";
    static final String SUCCESSFUL_UNTRACKING_MSG = "This link is no longer being tracked!";
    static final String ABSENT_UNTRACKING_MSG = "This link has not been tracked";
    static final String NOT_SUPPORTED_LINK_MSG = "This link is not supported";

    public String generateResponseText(@NotNull Update update) {
        long chatId = update.message().chat().id();
        String text = update.message().text();

        Optional<TelegramCommand> command = commandHandler.getCommandByName(text);

        if (command.isPresent()) {
            return command.get().handle(update);
        } else {
            return generateNonCommandText(chatId, text);
        }
    }

    private String generateNonCommandText(long chatId, String text) {
        Optional<User> initiator = userService.findById(chatId);

        if (initiator.isEmpty()) {
            return UNKNOWN_USER_MSG;
        }

        User user = initiator.get();

        try {
            String uri;
            String scheme = "https://";

            if (text.startsWith(scheme)) {
                uri = text;
            } else {
                uri = scheme + text;
            }

            return generateLinkValidatorText(user, URI.create(uri));
        } catch (IllegalArgumentException e) {
            return INVALID_LINK_MSG;
        }
    }

    private String generateLinkValidatorText(@NotNull User user, URI uri) {
        if (user.isAwaitingTrackingLink()) {
            return generateWaitingLinkForTrackingText(user, uri);
        }

        if (user.isAwaitingUnTrackingLink()) {
            return generateWaitingLinkForUntrackingText(user, uri);
        }

        return INVALID_COMMAND_MSG;
    }

    private String generateWaitingLinkForTrackingText(User user, URI url) {
        if (linkValidator.isLinkValid(url)) {
            return userService.addLink(user, url)
                ? SUCCESSFUL_TRACKING_MSG
                : DUPLICATE_TRACKING_MSG;
        }

        return NOT_SUPPORTED_LINK_MSG;
    }

    private String generateWaitingLinkForUntrackingText(User user, URI url) {
        if (linkValidator.isLinkValid(url)) {
            return userService.deleteLink(user, url)
                ? SUCCESSFUL_UNTRACKING_MSG
                : ABSENT_UNTRACKING_MSG;

        }

        return NOT_SUPPORTED_LINK_MSG;
    }
}
