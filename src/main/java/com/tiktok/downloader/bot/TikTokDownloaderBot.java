package com.tiktok.downloader.bot;

import static com.tiktok.downloader.bot.MessageUtils.sendMessageError;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.tiktok.downloader.infrastructure.service.TikTokDownloaderService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TikTokDownloaderBot extends TelegramWebhookBot {

  private static final String ERROR_MESSAGE = "An error occurred while sending the message";
  private static final String ERROR_MESSAGE_WRONG_URL = "Please, make sure your link looks like this:\n" +
      "https://www.tiktok.com/@test/video/000000000000\n or\n" +
      "https://vt.tiktok.com/testtest/\n";
  private static final String TIK_TOK_PATTERN = "https://[a-zA-Z]+\\.tiktok\\.com/.+";

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

  @SneakyThrows
  @Override
  public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
    if ((update.hasMessage() && update.getMessage().hasText())) {
      final Message message = update.getMessage();
      final String messageText = message.getText();

      log.debug("Message from {} with content \"{}\" received.", message.getFrom().getId(), messageText);

      SECONDS.sleep(10);

      try {
        if (checkCorrectUrl(messageText)) {
          execute(tikTokDownloaderService.sendTikTok(update));
        } else {
          execute(sendMessageError(message, ERROR_MESSAGE_WRONG_URL));
        }
      } catch (Exception exception) {
        log.debug("TikTok Downloader exception: {}", exception.getMessage());
        try {
          execute(sendMessageError(message, ERROR_MESSAGE));
        } catch (TelegramApiException e) {
          e.printStackTrace();
        }

      }
    }
    return null;
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
  public String getBotPath() {
    return webHookPath;
  }

  private boolean checkCorrectUrl(String message) {
    final Pattern pattern = Pattern.compile(TIK_TOK_PATTERN);
    final Matcher matcher = pattern.matcher(message);
    return matcher.matches();
  }
}
