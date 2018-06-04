package com.example.asus1.teacherbyvideo.Encoder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.opengl.EGLContext;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;

public class MediaVideoEncoder extends MediaEncoder {

    private static final String TAG = "MediaVideoEncoder";

    private static final String MIME_TYPE = "video/avc";

    private static final int FRAME_RATE = 25;
    private static final float BPP = 0.25f;

    private final int mWidth;
    private final int mHeight;
    private RenderHandler mRenderHandler;
    private Surface mSurface;

    public MediaVideoEncoder(MediaMuxerWrapper muxer, MediaEncoderListener listener
            ,int width,int height) {
        super(muxer, listener);
        mWidth = width;
        mHeight = height;
        mRenderHandler = RenderHandler.createHandler(TAG);

    }

    public boolean frameAvailableSoon(final float[] tex_matrix) {
        boolean result;
        if (result = super.frameAvailableSoon())
            mRenderHandler.draw(tex_matrix);
        return result;
    }

    public boolean frameAvailableSoon(final float[] tex_matrix, final float[] mvp_matrix) {
        boolean result;
        if (result = super.frameAvailableSoon())
            mRenderHandler.draw(tex_matrix, mvp_matrix);
        return result;
    }

    @Override
    public boolean frameAvailableSoon() {
        boolean result;
        if (result = super.frameAvailableSoon())
            mRenderHandler.draw(null);
        return result;
    }


    @Override
    void prepare() throws IOException {
        mTrackIndex = -1;
        mMuxerStarted = mIsEOS = false;
        MediaCodecInfo videoCodecInfo = selectVideoCodec(MIME_TYPE);
        if (videoCodecInfo == null) {
            Log.e(TAG, "Unable to find an appropriate codec for " + MIME_TYPE);
            return;
        }

        MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE,mWidth,mHeight);
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE,FRAME_RATE);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL,10);

        mMediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        mMediaCodec.configure(format,null,null,MediaCodec.CONFIGURE_FLAG_ENCODE);

        mSurface = mMediaCodec.createInputSurface();
        mMediaCodec.start();
        if(mListener !=null){
            try {
                mListener.onPrepares(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setEglContext(final EGLContext shared_context, final int tex_id) {
        mRenderHandler.setEglContext(shared_context, tex_id, mSurface, true);
    }

    @Override
    protected void release() {

        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mRenderHandler != null) {
            mRenderHandler.release();
            mRenderHandler = null;
        }
        super.release();
    }

    private int calcBitRate() {
        final int bitrate = (int)(BPP * FRAME_RATE * mWidth * mHeight);
        Log.i(TAG, String.format("bitrate=%5.2f[Mbps]", bitrate / 1024f / 1024f));
        return bitrate;
    }

    /**
     * select the first codec that match a specific MIME type
     * @param mimeType
     * @return null if no codec matched
     */

    protected static final MediaCodecInfo selectVideoCodec(final String mimeType) {


        // get the list of available codecs
        final int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            final MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (!codecInfo.isEncoder()) {	// skipp decoder
                continue;
            }
            // select first codec that match a specific MIME type and color format
            final String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {

                    final int format = selectColorFormat(codecInfo, mimeType);
                    if (format > 0) {
                        return codecInfo;
                    }
                }
            }
        }
        return null;
    }

    protected static final int selectColorFormat(final MediaCodecInfo codecInfo, final String mimeType) {
        int result = 0;
        final MediaCodecInfo.CodecCapabilities caps;
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            caps = codecInfo.getCapabilitiesForType(mimeType);
        } finally {
            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        }
        int colorFormat;
        for (int i = 0; i < caps.colorFormats.length; i++) {
            colorFormat = caps.colorFormats[i];
            if (isRecognizedViewoFormat(colorFormat)) {
                if (result == 0)
                    result = colorFormat;
                break;
            }
        }
        if (result == 0)
            Log.e(TAG, "couldn't find a good color format for " + codecInfo.getName() + " / " + mimeType);
        return result;
    }

    protected static int[] recognizedFormats;
    static {
        recognizedFormats = new int[] {
//        	MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar,
//        	MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar,
//        	MediaCodecInfo.CodecCapabilities.COLOR_QCOM_FormatYUV420SemiPlanar,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface,
        };
    }

    private static final boolean isRecognizedViewoFormat(final int colorFormat) {

        final int n = recognizedFormats != null ? recognizedFormats.length : 0;
        for (int i = 0; i < n; i++) {
            if (recognizedFormats[i] == colorFormat) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void singalEndOfInputStream() {
        mMediaCodec.signalEndOfInputStream();	// API >= 18
        mIsEOS = true;
    }

}
