package kz.append.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TelegramBot extends TelegramLongPollingBot {

    private static final String TRACKS_DIRECTORY = "tracks/";

    @Override
    public String getBotUsername() {
        return "MusicBoost_Bot";
    }

    @Override
    public String getBotToken() {
        return "6663067657:AAGhbHVgzbH_8Vrl9H6vROFpT44B8qeR-LE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {

                SendMessage startMessage = new SendMessage();
                startMessage.setChatId(chatId);
                startMessage.setText("Привет, дорогой пользователь! Перейдем к делу.");

                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                InlineKeyboardButton listenButton = new InlineKeyboardButton("Слушать");
                listenButton.setCallbackData("start_listening");
                keyboardMarkup.setKeyboard(List.of(List.of(listenButton)));
                startMessage.setReplyMarkup(keyboardMarkup);

                try {
                    execute(startMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    System.err.println("Error sending start message: " + e.getMessage());
                }
                return;
            }


            if (!messageText.equals("/start")) {
                File file = new File(TRACKS_DIRECTORY + messageText);

                if (file.exists() && file.canRead()) {

                    SendAudio sendAudio = new SendAudio();
                    sendAudio.setChatId(chatId);
                    sendAudio.setAudio(new org.telegram.telegrambots.meta.api.objects.InputFile(file));
                    sendAudio.setCaption("Вот ваш трек: " + file.getName());

                    try {
                        execute(sendAudio);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                        System.err.println("Error sending audio: " + e.getMessage());
                    }
                } else {
                    SendMessage errorMessage = new SendMessage();
                    errorMessage.setChatId(chatId);
                    errorMessage.setText("Ошибка: трек не найден или не доступен.");

                    try {
                        execute(errorMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                        System.err.println("Error sending error message: " + e.getMessage());
                    }
                }
            }
        }

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            if ("start_listening".equals(callbackData)) {
                try {

                    DeleteMessage deleteMessage = new DeleteMessage();
                    deleteMessage.setChatId(String.valueOf(chatId)); // Преобразуем chatId в строку
                    deleteMessage.setMessageId(messageId);

                    execute(deleteMessage);


                    SendMessage instructionMessage = new SendMessage();
                    instructionMessage.setChatId(chatId);
                    instructionMessage.setText("Вы можете начать вводить название трека, чтобы получить его или же перейдите в инлайн режим используя @Название_бота");

                    execute(instructionMessage);


                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    System.err.println("Error processing callback: " + e.getMessage());
                }
            }
        }

        if (update.hasInlineQuery()) {
            InlineQuery inlineQuery = update.getInlineQuery();
            String query = inlineQuery.getQuery();


            List<InlineQueryResult> results = new ArrayList<>();
            File[] files = getTracksFromDirectory();

            if (files != null) {
                for (File file : files) {

                    String cleanFileName = file.getName().replaceAll("[^a-zA-Z0-9_]", "_");


                    String uniqueId = UUID.randomUUID().toString();


                    InlineQueryResultArticle article = new InlineQueryResultArticle();
                    article.setId(uniqueId);
                    article.setTitle(file.getName());
                    article.setDescription("Нажмите, чтобы выбрать трек");


                    article.setInputMessageContent(new InputTextMessageContent(file.getName()));

                    results.add(article);
                }

                try {
                    AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
                    answerInlineQuery.setInlineQueryId(inlineQuery.getId());
                    answerInlineQuery.setResults(results);
                    execute(answerInlineQuery);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                    System.err.println("Error answering inline query: " + e.getMessage());
                }
            }
        }
    }

    private File[] getTracksFromDirectory() {
        File directory = new File(TRACKS_DIRECTORY);
        if (directory.exists() && directory.isDirectory()) {
            return directory.listFiles((dir, name) -> name.endsWith(".mp3"));
        }
        return new File[0];
    }
}