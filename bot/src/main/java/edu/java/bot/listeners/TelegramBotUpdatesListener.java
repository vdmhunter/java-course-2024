package edu.java.bot.listeners;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.services.MessageService;
import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot bot;
    private final MessageService service;
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    public TelegramBotUpdatesListener(@NotNull TelegramBot bot, MessageService service) {
        bot.setUpdatesListener(this);

        this.bot = bot;
        this.service = service;
    }

    @Override
    public int process(@NotNull List<Update> list) {
        for (Update update : list) {
            if (update.message() != null && update.message().chat() != null) {
                long chatId = update.message().chat().id();

                SendMessage message = new SendMessage(
                    chatId, service.generateResponseText(update)
                );

                bot.execute(message, createCallback());
            } else {
                LOGGER.warn("Received update without a message: {}", update);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Contract(value = " -> new", pure = true)
    private static @NotNull Callback<SendMessage, SendResponse> createCallback() {
        return new Callback<>() {
            @Override
            public void onResponse(SendMessage request, SendResponse response) {
                LOGGER.info("Response sent for request ({}) was ({})", request, response.message().text());
            }

            @Override
            public void onFailure(SendMessage request, IOException e) {
                LOGGER.error("The request was not executed: {}", e.getMessage());
            }
        };
    }
}
