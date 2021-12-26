package com.tiktok.downloader.downloader.parser;

import com.tiktok.downloader.downloader.model.TikTokVideo;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class VideoParser implements Parser {

    private static final String ITEMS = "items";
    private static final String AUTHOR = "author";
    private static final String VIDEO = "video";
    private static final String DOWNLOAD_ADDRESS = "downloadAddr";
    private static final String UNIQUE_ID = "uniqueId";
    private static final String NICKNAME = "nickname";

    public TikTokVideo parse(String fullVideoInfo) {
        final JSONObject videoInfo = getJsonObjectFromString(fullVideoInfo);
        return parseToObject(videoInfo);
    }

    private JSONObject getJsonObjectFromString(String fullVideoInfo) {
        return new JSONObject(fullVideoInfo);
    }

    private TikTokVideo parseToObject(JSONObject json) {
        if (json.isEmpty()) {
            return null;
        } else {
            final JSONObject items = json.getJSONArray(ITEMS).getJSONObject(0);
            final JSONObject author = items.getJSONObject(AUTHOR);

            final String video = items.getJSONObject(VIDEO).getString(DOWNLOAD_ADDRESS);
            final String authorUniqueId = author.getString(UNIQUE_ID);
            final String nickname = author.getString(NICKNAME);

            return new TikTokVideo(nickname, authorUniqueId, video);
        }
    }
}
