package edu.java.bot.listeners;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.commands.TelegramCommand;
import java.util.List;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
    private final TelegramBot bot;
    private final List<TelegramCommand> commands;

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        bot.execute(generateCommandMenu());
    }

    @Contract(" -> new")
    private @NotNull SetMyCommands generateCommandMenu() {
        var botCommands = new BotCommand[commands.size()];

        for (int i = 0; i < commands.size(); i++) {
            TelegramCommand command = commands.get(i);
            botCommands[i] = new BotCommand(command.command(), command.description());
        }

        return new SetMyCommands(botCommands);
    }
}
