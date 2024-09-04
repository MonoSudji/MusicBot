package kz.append;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import kz.append.service.AudioService;
import kz.append.service.CallbackService;
import kz.append.service.InlineQueryService;

public class Main {

    private static final String BOT_TOKEN = "6993661970:AAFdWnjr4jD842GIPD5u4jyFCbMakKWo8Tg";

    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot(BOT_TOKEN);
        AudioService audioService = new AudioService(bot);
        CallbackService callbackService = new CallbackService(bot);
        InlineQueryService inlineQueryService = new InlineQueryService(bot);

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null) {
                    audioService.handleMessage(update);
                }
                if (update.callbackQuery() != null) {
                    callbackService.handleCallbackQuery(update);
                }
                if (update.inlineQuery() != null) {
                    inlineQueryService.handleInlineQuery(update);
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}
