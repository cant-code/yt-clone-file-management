package com.cantcode.yt.filemanagement.webapp.controller;

public class Range {
    private final long start;

    private final long end;

    public Range(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getRangeStart() {
        return start;
    }

    public long getRangeEnd(long fileSize) {
        return Math.min(end, fileSize - 1);
    }

    public static Range parseHttpRangeString(String httpRangeString, int defaultChunkSize) {
        if (httpRangeString == null) {
            return new Range(0, defaultChunkSize);
        }
        int dashIndex = httpRangeString.indexOf("-");
        long startRange = Long.parseLong(httpRangeString.substring(6, dashIndex));
        String endRangeString = httpRangeString.substring(dashIndex + 1);
        if (endRangeString.isEmpty()) {
            return new Range(startRange, startRange + defaultChunkSize);
        }
        long endRange = Long.parseLong(endRangeString);
        return new Range(startRange, endRange);
    }
}
