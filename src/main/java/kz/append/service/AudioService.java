package kz.append.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.request.SendMessage;
import kz.append.gui.BotUICustomizer;

import java.io.File;

public class AudioService {

    private static final String TRACKS_DIRECTORY = "tracks/";
    final TelegramBot bot;

    public AudioService(TelegramBot bot) {
        this.bot = bot;
    }

    public void handleMessage(Update update) {
        String messageText = update.message().text().trim();
        long chatId = update.message().chat().id();

        if ("/start".equals(messageText)) {
            SendMessage startMessage = BotUICustomizer.createStartMessage(chatId);
            bot.execute(startMessage);
        } else {
            File file = new File(TRACKS_DIRECTORY + messageText);
            if (file.exists() && file.canRead()) {
                SendAudio sendAudio = new SendAudio(chatId, file.getAbsolutePath());
                bot.execute(sendAudio);
            } else {
                SendMessage errorMessage = BotUICustomizer.createErrorMessage(chatId, "Ошибка: трек не найден или не доступен.");
                bot.execute(errorMessage);
            }
        }
    }
}
