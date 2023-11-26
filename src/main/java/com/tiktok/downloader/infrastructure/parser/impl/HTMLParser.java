package com.tiktok.downloader.infrastructure.parser.impl;

import com.tiktok.downloader.infrastructure.parser.Parser;
import java.util.List;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class HTMLParser implements Parser {

  private static final String CLASS_NAME = "flex-1 result_overlay_buttons pure-u-1 pure-u-sm-1-2";

  @Override
  public String getVideoLink(Document document) {
    final Elements parentElement = document.getElementsByClass(CLASS_NAME);
    final List<Node> childNodes = parentElement.get(0).childNodes();
    final Attributes firsSource = childNodes.get(1).attributes();
    return firsSource.asList().get(0).getValue();
  }
}
