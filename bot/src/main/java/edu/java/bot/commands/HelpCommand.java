package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HelpCommand implements Command {
    private final List<Command> commands;
    static final String HELP_MSG = "All commands:" + System.lineSeparator();
    static final String COMMAND_DESC_MSG = "%s — %s" + System.lineSeparator();

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Show command list";
    }

    @Override
    public String handle(Update update) {
        return generateMessage();
    }

    private @NotNull String generateMessage() {
        var text = new StringBuilder();
        text.append(HELP_MSG);

        commands.forEach(command ->
            text.append(
                COMMAND_DESC_MSG.formatted(
                    command.command(), command.description()
                ))
        );

        return text.toString();
    }
}
