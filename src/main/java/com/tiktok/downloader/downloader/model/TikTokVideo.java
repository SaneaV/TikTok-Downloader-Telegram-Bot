package com.tiktok.downloader.downloader.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TikTokVideo {

    private final String nickName;
    private final String authorUniqueId;
    private final String videoDownloadAddress;
}
