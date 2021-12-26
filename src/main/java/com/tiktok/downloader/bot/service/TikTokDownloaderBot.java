package com.tiktok.downloader.bot.service;

import com.tiktok.downloader.downloader.service.TikTokDownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.tiktok.downloader.bot.utils.MessageUtils.sendMessageError;

@Component
public class TikTokDownloaderBot extends TelegramWebhookBot {

    private static final String ERROR_MESSAGE = "При отправке сообщения произошла ошибка";

    private final String webHookPath;
    private final String botUserName;
    private final String botToken;
    private final TikTokDownloaderService tikTokDownloaderService;

    @Autowired
    public TikTokDownloaderBot(
            @Value("${telegrambot.webHookPath}") String webHookPath,
            @Value("${telegrambot.userName}") String botUserName,
            @Value("${telegrambot.botToken}") String botToken,
            TikTokDownloaderService tikTokDownloaderService) {
        this.webHookPath = webHookPath;
        this.botUserName = botUserName;
        this.botToken = botToken;
        this.tikTokDownloaderService = tikTokDownloaderService;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if ((update.hasMessage() && update.getMessage().hasText())) {
            try {
                execute(tikTokDownloaderService.sendTikTok(update));
            } catch (Exception exception) {
                try {
                    execute(sendMessageError(update.getMessage(), ERROR_MESSAGE));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }
}
