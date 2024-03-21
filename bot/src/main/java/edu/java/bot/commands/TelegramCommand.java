package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;

public interface TelegramCommand {
    String command();

    String description();

    String handle(Update update);
}
