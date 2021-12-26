package com.tiktok.downloader.downloader.repository.impl;

import com.tiktok.downloader.downloader.model.TikTokVideo;
import com.tiktok.downloader.downloader.parser.VideoParser;
import com.tiktok.downloader.downloader.repository.TikTokDownloaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static reactor.retry.Repeat.onlyIf;

@Slf4j
@Component
@RequiredArgsConstructor
public class TikTokDownloaderRepositoryImpl implements TikTokDownloaderRepository {

    private static final String TTLOADER_URI =
            "https://ttloader.com/wp-json/wppress/tiktok-downloader/videos?search=%s&type=video_url";
    private static final String FALSE = "false";

    private final WebClient webClient;
    private final VideoParser videoParser;

    @Override
    public TikTokVideo getVideo(String videoLink) {
        log.debug("Message with content \"{}\" at the repository level.", videoLink);
        final String ttLoaderLink = getVideoDownloadUrl(videoLink);

        return webClient.post()
                .uri(ttLoaderLink)
                .retrieve()
                .bodyToMono(String.class)
                .retry(5)
                .filter(response -> !response.equalsIgnoreCase(FALSE))
                .repeatWhenEmpty(onlyIf(r -> true)
                        .fixedBackoff(ofSeconds(1))
                        .timeout(ofSeconds(10)))
                .map(videoParser::parse)
                .block();
    }

    private String getVideoDownloadUrl(String videoLink) {
        return format(TTLOADER_URI, videoLink);
    }
}
