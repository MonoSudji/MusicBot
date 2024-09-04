package kz.append.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import kz.append.gui.BotUICustomizer;

public class CallbackService {

    private final TelegramBot bot;

    public CallbackService(TelegramBot bot) {
        this.bot = bot;
    }

    public void handleCallbackQuery(Update update) {
        String callbackData = update.callbackQuery().data();
        long chatId = update.callbackQuery().message().chat().id();
        Integer messageId = update.callbackQuery().message().messageId();

        if ("start_listening".equals(callbackData)) {
            DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
            bot.execute(deleteMessage);

            SendMessage instructionMessage = BotUICustomizer.createInstructionMessage(chatId);
            bot.execute(instructionMessage);
        }
    }
}
