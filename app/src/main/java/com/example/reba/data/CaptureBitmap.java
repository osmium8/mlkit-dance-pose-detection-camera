package com.example.reba.data;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CaptureBitmap {
    private AV_FrameCapture mFrameCapture = null;
    boolean USE_MEDIA_META_DATA_RETRIEVER = false;

    public Bitmap captureFrame(String VIDEO_FILE_PATH, long SNAPSHOT_DURATION_IN_MILLIS, int SNAPSHOT_WIDTH, int SNAPSHOT_HEIGHT) {

        // getFrameAtTimeByMMDR & getFrameAtTimeByFrameCapture function uses a micro sec 1millisecond = 1000 microseconds

        Bitmap bmp = USE_MEDIA_META_DATA_RETRIEVER ? getFrameAtTimeByMMDR(VIDEO_FILE_PATH, (SNAPSHOT_DURATION_IN_MILLIS * 1000))
                : getFrameAtTimeByFrameCapture(VIDEO_FILE_PATH, (SNAPSHOT_DURATION_IN_MILLIS * 1000), SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);

        if (mFrameCapture != null) {
            mFrameCapture.release();
        }

        return bmp;
    }

    private Bitmap getFrameAtTimeByMMDR(String path, long time) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        Bitmap bmp = mmr.getFrameAtTime(time, MediaMetadataRetriever.OPTION_CLOSEST);
        mmr.release();
        return bmp;
    }

    private Bitmap getFrameAtTimeByFrameCapture(String path, long time, int snapshot_width, int snapshot_height) {
        mFrameCapture = new AV_FrameCapture();
        mFrameCapture.setDataSource(path);
        mFrameCapture.setTargetSize(snapshot_width, snapshot_height);
        mFrameCapture.init();
        return mFrameCapture.getFrameAtTime(time);
    }
}
