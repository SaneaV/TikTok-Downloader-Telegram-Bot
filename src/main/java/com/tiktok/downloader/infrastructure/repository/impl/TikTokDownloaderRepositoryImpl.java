package com.tiktok.downloader.infrastructure.repository.impl;

import static org.jsoup.Connection.Method.POST;

import com.tiktok.downloader.domain.TikTok;
import com.tiktok.downloader.infrastructure.parser.Parser;
import com.tiktok.downloader.infrastructure.repository.TikTokDownloaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TikTokDownloaderRepositoryImpl implements TikTokDownloaderRepository {

  private static final String TIKTOK_DOWNLOAD_LINK = "https://tiktokdownload.online/abc?url=dl";
  private static final String ID = "id";
  private static final String AT = "@";
  private static final String SLASH = "/";

  private final Parser parser;

  @SneakyThrows
  @Override
  public TikTok getTikTok(String link) {
    log.debug("Message with content \"{}\" at the repository level.", link);

    final Document html = Jsoup.connect(TIKTOK_DOWNLOAD_LINK)
        .timeout(10000)
        .method(POST)
        .data(ID, link)
        .execute()
        .parse();

    final String videoLink = parser.getVideoLink(html);
    return new TikTok(getUserId(link), videoLink);
  }

  private String getUserId(String tiktokLink) {
    int start = tiktokLink.indexOf(AT);
    int end = tiktokLink.indexOf(SLASH, start);
    return tiktokLink.substring(start, end);
  }
}
