package edu.java.bot.configs;

import com.pengrad.telegrambot.TelegramBot;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Bean
    public TelegramBot telegramBot(@NotNull ApplicationConfig applicationConfig) {
        return new TelegramBot(applicationConfig.telegramToken());
    }
}

