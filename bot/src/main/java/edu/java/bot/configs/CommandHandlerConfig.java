package edu.java.bot.configs;

import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UnTrackCommand;
import edu.java.bot.handlers.CommandHandler;
import edu.java.bot.services.UserService;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("SpellCheckingInspection")
@Configuration
public class CommandHandlerConfig {
    @Bean
    public CommandHandler commandHandler(UserService userService) {
        var start = new StartCommand(userService);
        var track = new TrackCommand(userService);
        var untrack = new UnTrackCommand(userService);
        var list = new ListCommand(userService);
        var help = new HelpCommand(
            List.of(
                start,
                track,
                untrack,
                list
            )
        );

        return new CommandHandler(
            Map.of(
                "/start", start,
                "/track", track,
                "/untrack", untrack,
                "/list", list,
                "/help", help
            )
        );
    }
}
