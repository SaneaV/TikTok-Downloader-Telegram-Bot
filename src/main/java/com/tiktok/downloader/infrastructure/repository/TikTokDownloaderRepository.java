package com.tiktok.downloader.infrastructure.repository;

import com.tiktok.downloader.domain.TikTok;

public interface TikTokDownloaderRepository {

    TikTok getTikTok(String link);
}
