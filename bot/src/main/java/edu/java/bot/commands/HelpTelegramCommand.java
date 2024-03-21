package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HelpTelegramCommand implements TelegramCommand {
    private final List<TelegramCommand> telegramCommands;
    static final String LINE_SEPARATOR = System.lineSeparator();
    static final String HELP_MSG = "All commands:" + LINE_SEPARATOR;
    static final String COMMAND_DESC_MSG = "%s — %s" + LINE_SEPARATOR;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Show list of commands";
    }

    @Override
    public String handle(Update update) {
        return generateMessage();
    }

    private @NotNull String generateMessage() {
        var sb = new StringBuilder();
        sb.append(HELP_MSG);

        telegramCommands.forEach(command ->
            sb.append(
                COMMAND_DESC_MSG.formatted(
                    command.command(), command.description()
                ))
        );

        return sb.toString();
    }
}
