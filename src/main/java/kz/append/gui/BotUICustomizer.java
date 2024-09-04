package kz.append.gui;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;

public class BotUICustomizer {

    public static SendMessage createStartMessage(long chatId) {
        String welcomeText = "Привет, дорогой пользователь! Перейдем к делу.";
        String buttonText = "Слушать";
        ReplyKeyboardMarkup replyKeyboard = new ReplyKeyboardMarkup(new String[]{buttonText})
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);

        return new SendMessage(chatId, welcomeText).replyMarkup(replyKeyboard);
    }

    public static SendMessage createInstructionMessage(long chatId) {
        String instructionText = "Вы можете начать вводить название трека, чтобы получить его или же перейдите в инлайн режим используя @Название_бота";
        return new SendMessage(chatId, instructionText);
    }

    public static SendMessage createErrorMessage(long chatId, String errorMessage) {
        return new SendMessage(chatId, errorMessage);
    }

    public static ReplyKeyboardMarkup createCustomKeyboard(String... buttons) {
        return new ReplyKeyboardMarkup(buttons)
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);
    }

    public static SendMessage removeKeyboard(long chatId) {
        return new SendMessage(chatId, "Клавиатура удалена").replyMarkup(new ReplyKeyboardRemove(true));
    }
}
