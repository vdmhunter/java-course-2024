package edu.java.bot.handlers;

import edu.java.bot.commands.TelegramCommand;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public record TelegramCommandHandler(Map<String, TelegramCommand> commands) {
    public Optional<TelegramCommand> getCommandByName(String command) {
        return commands.containsKey(command)
            ? Optional.of(commands.get(command))
            : Optional.empty();
    }
}
