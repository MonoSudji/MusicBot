package kz.append;

import kz.append.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot bot = new TelegramBot();

            try {
                botsApi.registerBot(bot);
            } catch (TelegramApiException e) {
                System.out.println("Error registering bot: " + e.getMessage());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}