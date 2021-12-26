package com.tiktok.downloader.downloader.repository;

import com.tiktok.downloader.downloader.model.TikTokVideo;

public interface TikTokDownloaderRepository {

    TikTokVideo getVideo(String videoLink);
}
