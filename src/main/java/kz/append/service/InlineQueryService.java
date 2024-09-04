package kz.append.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineQueryResult;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.model.request.InputTextMessageContent;
import com.pengrad.telegrambot.request.AnswerInlineQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InlineQueryService {

    private static final String TRACKS_DIRECTORY = "tracks/";
    private final TelegramBot bot;

    public InlineQueryService(TelegramBot bot) {
        this.bot = bot;
    }

    public void handleInlineQuery(Update update) {
        InlineQuery inlineQuery = update.inlineQuery();
        String query = inlineQuery.query();

        List<InlineQueryResult> results = new ArrayList<>();
        File[] files = getTracksFromDirectory();

        if (files != null) {
            for (File file : files) {
                String cleanFileName = file.getName().replaceAll("[^a-zA-Z0-9_]", "_");
                String uniqueId = UUID.randomUUID().toString();
                InlineQueryResultArticle article = new InlineQueryResultArticle(uniqueId, file.getName(), "Нажмите, чтобы выбрать трек");
                article.inputMessageContent(new InputTextMessageContent(file.getName()));
                results.add(article);
            }

            AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery(inlineQuery.id(), results.toArray(new InlineQueryResult[0]));
            bot.execute(answerInlineQuery);
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
