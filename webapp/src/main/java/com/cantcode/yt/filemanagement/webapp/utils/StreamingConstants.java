package com.cantcode.yt.filemanagement.webapp.utils;

import com.cantcode.yt.filemanagement.webapp.controller.Range;

public class StreamingConstants {

    public static final int CHUNK_SIZE = 100000;
    public static final String MP4_MEDIA_TYPE = "video/mp4";
    public static final String ACCEPTED_RANGE_BYTES = "bytes";

    public static String calculateContentLengthHeader(final Range range, final Long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    public static String constructContentRangeHeader(final Range range, final Long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }

    private StreamingConstants() {
    }
}
