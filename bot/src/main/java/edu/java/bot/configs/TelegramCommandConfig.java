package edu.java.bot.configs;

import edu.java.bot.commands.HelpTelegramCommand;
import edu.java.bot.commands.ListTelegramCommand;
import edu.java.bot.commands.StartTelegramCommand;
import edu.java.bot.commands.TelegramCommand;
import edu.java.bot.commands.TrackTelegramCommand;
import edu.java.bot.commands.UnTrackTelegramCommand;
import edu.java.bot.services.UserService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramCommandConfig {
    @Bean
    public StartTelegramCommand startTelegramCommand(UserService userService) {
        return new StartTelegramCommand(userService);
    }

    @Bean
    public TrackTelegramCommand trackTelegramCommand(UserService userService) {
        return new TrackTelegramCommand(userService);
    }

    @Bean
    public UnTrackTelegramCommand unTrackTelegramCommand(UserService userService) {
        return new UnTrackTelegramCommand(userService);
    }

    @Bean
    public ListTelegramCommand listTelegramCommand(UserService userService) {
        return new ListTelegramCommand(userService);
    }

    @Bean
    public HelpTelegramCommand helpTelegramCommand(List<TelegramCommand> telegramCommands) {
        return new HelpTelegramCommand(telegramCommands);
    }
}
