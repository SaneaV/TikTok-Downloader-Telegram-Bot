package com.tiktok.downloader.downloader.service.impl;

import com.tiktok.downloader.downloader.model.TikTokVideo;
import com.tiktok.downloader.downloader.repository.TikTokDownloaderRepository;
import com.tiktok.downloader.downloader.service.TikTokDownloaderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.tiktok.downloader.bot.utils.MessageUtils.sendVideo;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class TikTokDownloaderServiceImpl implements TikTokDownloaderService {

    private static final String PATH_NAME = "Video";
    private static final String MESSAGE_TEXT = "Model(s) TikTok - [%s](https://www.tiktok.com/@%s)\n\n" +
            "Instagram [Chisinau_Girls](https://www.instagram.com/chisinau_girls/)\n" +
            "Instagram [Chisinau_Topless](https://www.instagram.com/chisinau_topless/)";

    private final TikTokDownloaderRepository tikTokDownloaderRepository;

    @Override
    public SendVideo sendTikTok(Update update) throws IOException {
        final Message message = update.getMessage();
        final TikTokVideo tikTokVideo = tikTokDownloaderRepository.getVideo(message.getText());
        final String formattedMessage = format(MESSAGE_TEXT, tikTokVideo.getNickName(), tikTokVideo.getAuthorUniqueId());

        final URL url = new URL(tikTokVideo.getVideoDownloadAddress());
        final File file = new File(PATH_NAME);
        FileUtils.copyURLToFile(url, file);
        final InputFile inputFile = new InputFile(file);

        return sendVideo(message, formattedMessage, inputFile);
    }
}
