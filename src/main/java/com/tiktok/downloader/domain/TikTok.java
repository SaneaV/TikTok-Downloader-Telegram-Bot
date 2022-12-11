package com.tiktok.downloader.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TikTok {

  private final String userId;
  private final String videoUrl;
}
