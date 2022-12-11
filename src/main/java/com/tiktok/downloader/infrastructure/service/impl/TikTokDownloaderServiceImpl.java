package com.tiktok.downloader.infrastructure.service.impl;

import static com.tiktok.downloader.bot.MessageUtils.sendVideo;
import static java.lang.String.format;

import com.tiktok.downloader.domain.TikTok;
import com.tiktok.downloader.infrastructure.repository.TikTokDownloaderRepository;
import com.tiktok.downloader.infrastructure.service.TikTokDownloaderService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TikTokDownloaderServiceImpl implements TikTokDownloaderService {

  private static final String PATH_NAME = "Video.mp4";
  private static final String MESSAGE_TEXT = "Girl's TikTok account - [%s](https://www.tiktok.com/%s)\n\n" +
      "Instagram [Chisinau_Girls](https://www.instagram.com/chisinau_girls/)";
  private static final String ERROR_DURING_CONVERTING_OF_FILE = "The error occurred during converting of url to input file";

  private final TikTokDownloaderRepository tikTokDownloaderRepository;

  @Override
  public SendVideo sendTikTok(Update update) {
    final Message message = update.getMessage();
    final Long userId = message.getFrom().getId();
    final String messageText = message.getText();

    log.debug("Message from {} with content \"{}\" at the service layer.", userId, messageText);

    final TikTok tikTok = tikTokDownloaderRepository.getTikTok(messageText);
    final String formattedMessage = format(MESSAGE_TEXT, tikTok.getUserId(), tikTok.getUserId());

    log.debug("Message from {} with content \"{}\" sending from the service layer.", userId, messageText);

    return sendVideo(message, formattedMessage, convertToInputFile(tikTok.getVideoUrl()));
  }

  private InputFile convertToInputFile(String videoUrl) {
    try {
      final URL url = new URL(videoUrl);
      final File file = new File(PATH_NAME);
      FileUtils.copyURLToFile(url, file);
      return new InputFile(file);
    } catch (IOException e) {
      throw new RuntimeException(ERROR_DURING_CONVERTING_OF_FILE);
    }
  }
}
