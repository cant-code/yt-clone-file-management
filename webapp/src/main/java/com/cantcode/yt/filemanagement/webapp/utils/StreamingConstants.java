package com.cantcode.yt.filemanagement.webapp.utils;

import com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus;

import java.util.List;

import static com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus.PARTIALLY_PROCESSED;
import static com.cantcode.yt.filemanagement.webapp.enums.TranscodingStatus.PROCESSED;

public class StreamingConstants {

    public static final int CHUNK_SIZE = 100000;
    public static final String MP4_MEDIA_TYPE = "video/mp4";
    public static final String ACCEPTED_RANGE_BYTES = "bytes";
    public static final List<String> ACCEPTED_QUALITY_PARAM = List.of("360", "480", "720");
    public static final List<TranscodingStatus> PROCESSED_STATUS = List.of(PROCESSED, PARTIALLY_PROCESSED);

    public static String calculateContentLengthHeader(final Range range, final Long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    public static String constructContentRangeHeader(final Range range, final Long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }

    private StreamingConstants() {
    }
}
