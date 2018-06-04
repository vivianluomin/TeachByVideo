package com.example.asus1.teacherbyvideo.Encoder;

import android.icu.text.MessageFormat;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.Policy;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MediaMuxerWrapper {

    private static final String TAG = "MediaMuxerWrapper";

    private static final String DIR_NAME = "AVRecord";
    private static final SimpleDateFormat mDateTimeFormat =
            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

    private String mOutputPath;
    private final MediaMuxer mMediaMuxer;	// API >= 18
    private int mEncoderCount, mStatredCount;
    private boolean mIsStarted;
    private MediaEncoder mVideoEncoder, mAudioEncoder;

    public MediaMuxerWrapper(String ext) throws IOException{
        if(TextUtils.isEmpty(ext)) ext = ".mp4";
        try {
            mOutputPath = getCaptureFile(Environment.DIRECTORY_MOVIES, ext).toString();
        } catch (final NullPointerException e) {
            throw new RuntimeException("This app has no permission of writing external storage");
        }

        mMediaMuxer = new MediaMuxer(mOutputPath,MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        mEncoderCount = mStatredCount = 0;
        mIsStarted = false;
    }

    public String getOutputPath(){
        return mOutputPath;
    }

    public void prepare()throws IOException{
        if(mVideoEncoder !=null){
            mVideoEncoder.prepare();
        }
        if(mAudioEncoder!=null){
            mAudioEncoder.prepare();
        }
    }

    public void startRecoding(){
        if(mVideoEncoder !=null){
            mVideoEncoder.startRecording();
        }

        if (mAudioEncoder != null)
            mAudioEncoder.startRecording();
    }

    public void stopRecording() {
        if (mVideoEncoder != null)
            mVideoEncoder.stopRecording();
        mVideoEncoder = null;
        if (mAudioEncoder != null)
            mAudioEncoder.stopRecording();
        mAudioEncoder = null;
    }

    public synchronized boolean isStarted() {
        return mIsStarted;
    }

    void addEncoder(final MediaEncoder encoder) {
        if (encoder instanceof MediaVideoEncoder) {
            if (mVideoEncoder != null)
                throw new IllegalArgumentException("Video encoder already added.");
            mVideoEncoder = encoder;
        } else if (encoder instanceof MediaAudioEncoder) {
            if (mAudioEncoder != null)
                throw new IllegalArgumentException("Video encoder already added.");
            mAudioEncoder = encoder;
        } else
            throw new IllegalArgumentException("unsupported encoder");
        mEncoderCount = (mVideoEncoder != null ? 1 : 0) + (mAudioEncoder != null ? 1 : 0);
    }

    synchronized boolean start(){
        mStatredCount++;
        if(mEncoderCount>0 && mStatredCount == mEncoderCount){
            mMediaMuxer.start();
            mIsStarted = true;
            notifyAll();
        }

        return mIsStarted;
    }

    synchronized void stop() {

        mStatredCount--;
        if ((mEncoderCount > 0) && (mStatredCount <= 0)) {
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mIsStarted = false;

        }
    }

    synchronized int addTrack(MediaFormat format){
        if(mIsStarted) throw new IllegalStateException("muxer already started");

        int trackIx = mMediaMuxer.addTrack(format);
        return trackIx;
    }

    synchronized void writeSampleData(final int trackIndex, final ByteBuffer byteBuf, final MediaCodec.BufferInfo bufferInfo) {
        if (mStatredCount > 0)
            mMediaMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo);
    }

    /**
     * generate output file
     * @param type Environment.DIRECTORY_MOVIES / Environment.DIRECTORY_DCIM etc.
     * @param ext .mp4(.m4a for audio) or .png
     * @return return null when this app has no writing permission to external storage.
     */
    public static final File getCaptureFile(final String type, final String ext) {
        final File dir = new File(Environment.getExternalStoragePublicDirectory(type), DIR_NAME);
        Log.d(TAG, "path=" + dir.toString());
        dir.mkdirs();
        if (dir.canWrite()) {
            return new File(dir, getDateTimeString() + ext);
        }
        return null;
    }

    private static final String getDateTimeString() {
        final GregorianCalendar now = new GregorianCalendar();
        return mDateTimeFormat.format(now.getTime());
    }

}
