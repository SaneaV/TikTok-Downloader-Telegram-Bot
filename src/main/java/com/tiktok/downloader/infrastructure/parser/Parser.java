package com.tiktok.downloader.infrastructure.parser;

import org.jsoup.nodes.Document;

public interface Parser {

  String getVideoLink(Document document);
}
