package edu.java.bot.configs;

import edu.java.bot.commands.HelpTelegramCommand;
import edu.java.bot.commands.ListTelegramCommand;
import edu.java.bot.commands.StartTelegramCommand;
import edu.java.bot.commands.TrackTelegramCommand;
import edu.java.bot.commands.UnTrackTelegramCommand;
import edu.java.bot.handlers.TelegramCommandHandler;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpellCheckingInspection")
@Configuration
public class TelegramCommandHandlerConfig {
    @Bean
    public TelegramCommandHandler commandHandler(
        StartTelegramCommand startTelegramCommand,
        TrackTelegramCommand trackTelegramCommand,
        UnTrackTelegramCommand unTrackTelegramCommand,
        ListTelegramCommand listTelegramCommand,
        HelpTelegramCommand helpTelegramCommand
    ) {
        return new TelegramCommandHandler(
            Map.of(
                "/start", startTelegramCommand,
                "/track", trackTelegramCommand,
                "/untrack", unTrackTelegramCommand,
                "/list", listTelegramCommand,
                "/help", helpTelegramCommand
            )
        );
    }
}
