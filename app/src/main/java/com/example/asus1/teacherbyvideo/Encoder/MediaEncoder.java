package com.example.asus1.teacherbyvideo.Encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

public abstract class MediaEncoder implements Runnable {

    private static final String TAG = "MediaEncoder";

    protected static final int TIMEOUT_USEC = 10000;

    public interface MediaEncoderListener{
        void onPrepares(MediaEncoder encoder);
        void onStop(MediaEncoder encoder);
    }

    protected  final Object mSync = new Object();

    protected volatile boolean mIsCapturing;

    private int mRequestDrain;

    protected volatile boolean mRequestStop;

    protected boolean mIsEOS;//end od stream

    protected boolean mMuxerStarted;

    protected int mTrackIndex;

    protected MediaCodec mMediaCodec;

    protected final WeakReference<MediaMuxerWrapper> mWeakMuxer;

    private MediaCodec.BufferInfo mBufferInfo;
    protected final MediaEncoderListener mListener;

    public MediaEncoder(MediaMuxerWrapper muxer , MediaEncoderListener listener){
        if (listener == null) throw new NullPointerException("MediaEncoderListener is null");
        if (muxer == null) throw new NullPointerException("MediaMuxerWrapper is null");
        mWeakMuxer = new WeakReference<>(muxer);
        muxer.addEncoder(this);
        mListener = listener;
        synchronized (mSync){
            mBufferInfo = new MediaCodec.BufferInfo();
            new Thread(this,getClass().getSimpleName()).start();
            try {
                mSync.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public String getOutputPath(){
        MediaMuxerWrapper muxer = mWeakMuxer.get();
        return muxer!=null?muxer.getOutputPath():null;
    }

    public boolean frameAvailableSoon(){
        synchronized (mSync){
            if(mIsCapturing || mRequestStop){
                return false;
            }
            mRequestDrain++;
            mSync.notifyAll();
        }

        return true;
    }

    @Override
    public void run() {
        synchronized (mSync){
            mRequestStop = false;
            mRequestDrain = 0;
            mSync.notify();
        }

        boolean isRunning = true;
        boolean localRequestStop;
        boolean localRequestDrain;
        while (isRunning){
            synchronized (mSync){
                localRequestStop = mRequestStop;
                localRequestDrain = mRequestDrain>0;
                if(localRequestDrain){
                    mRequestDrain--;
                }
            }

            if (localRequestStop){
                drain();
                // request stop recording
                singalEndOfInputStream();
                // process output data again for EOS signale
                drain();
                // release all related objects
                release();
                break;
            }

            if(localRequestDrain){
                drain();
            }else {
                synchronized (mSync){
                    try {
                        mSync.wait();
                    }catch (InterruptedException e){

                    }
                }
            }
        }

        synchronized (mSync){
            mRequestStop = true;
            mIsCapturing = false;
        }
    }

    abstract void prepare()throws IOException;

    void startRecording(){
        synchronized (mSync){
            mIsCapturing = true;
            mRequestStop = false;
            mSync.notifyAll();
        }
    }

    void stopRecording(){
        synchronized (mSync){
            if(!mIsCapturing || mRequestStop){
                return;
            }
            mRequestStop = true;
            mSync.notifyAll();// We can not know when the encoding and writing finish.
            // so we return immediately after request to avoid delay of caller thread
        }
    }

    protected void release(){
        try {
            mListener.onStop(this);
        }catch (Exception e){

        }

        mIsCapturing  =false;
        if(mMediaCodec !=null){
            try {
                mMediaCodec.stop();
                mMediaCodec.release();
                mMediaCodec = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(mMuxerStarted){
            MediaMuxerWrapper muxer = mWeakMuxer!=null? mWeakMuxer.get():null;
            if(muxer!=null){
                try {
                    muxer.stop();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        mBufferInfo = null;
    }

    protected void singalEndOfInputStream(){
        encode(null,0,getPTSUs());
    }

    protected void encode(ByteBuffer buffer,int length,long presentationTimeUs){
        if(!mIsCapturing) return;
        ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
        while (mIsCapturing){
            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(TIMEOUT_USEC);
            if(inputBufferIndex>=0){
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                if(buffer !=null){
                    inputBuffer.put(buffer);
                }

                if(length<=0){
                    mIsEOS = true;
                    mMediaCodec.queueInputBuffer(inputBufferIndex,
                            0,0,presentationTimeUs,MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    break;
                }else {
                    mMediaCodec.queueInputBuffer(inputBufferIndex,
                            0,length,presentationTimeUs,0);
                }
                break;
            }else if (inputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER){
                // wait for MediaCodec encoder is ready to encode
                // nothing to do here because MediaCodec#dequeueInputBuffer(TIMEOUT_USEC)
                // will wait for maximum TIMEOUT_USEC(10msec) on each call
            }
        }
    }

    /**
     * drain encoded data and write them to muxer
     */

    protected void drain(){
        if(mMediaCodec == null) return;
        ByteBuffer[] encoderOutputBuffer = mMediaCodec.getOutputBuffers();
        int encoderStatus,count = 0;
        MediaMuxerWrapper muxer = mWeakMuxer.get();
        if(muxer == null){
            Log.w(TAG, "muxer is unexpectedly null");
            return;
        }

 LOOP:       while (mIsCapturing){
            encoderStatus = mMediaCodec.dequeueOutputBuffer(mBufferInfo,TIMEOUT_USEC);
            if(encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER){
                if(!mIsEOS){
                    if(++count>5)
                        break LOOP;
                }
            }else if(encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED){
                encoderOutputBuffer = mMediaCodec.getOutputBuffers();

            }else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
                if (mMuxerStarted) {	// second time request is error
                    throw new RuntimeException("format changed twice");
                }
                // get output format from codec and pass them to muxer
                // getOutputFormat should be called after INFO_OUTPUT_FORMAT_CHANGED otherwise crash.
                final MediaFormat format = mMediaCodec.getOutputFormat(); // API >= 16
                mTrackIndex = muxer.addTrack(format);
                mMuxerStarted = true;
                if(!muxer.start()){
                    synchronized (muxer){
                        while (!muxer.isStarted())
                            try {
                                muxer.wait(100);
                            }catch (InterruptedException e){
                            break LOOP;
                            }
                    }
                }
            }else if(encoderStatus<0){

            }else {
                ByteBuffer encodedData = encoderOutputBuffer[encoderStatus];
                if (encodedData == null) {
                    // this never should come...may be a MediaCodec internal error
                    throw new RuntimeException("encoderOutputBuffer " + encoderStatus + " was null");
                }

                if((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG)!=0){
                    mBufferInfo.size = 0;
                }

                if(mBufferInfo.size!=0){
                    count = 0;
                    if (!mMuxerStarted) {
                        // muxer is not ready...this will prrograming failure.
                        throw new RuntimeException("drain:muxer hasn't started");
                    }

                    // write encoded data to muxer(need to adjust presentationTimeUs.
                    mBufferInfo.presentationTimeUs = getPTSUs();
                    muxer.writeSampleData(mTrackIndex, encodedData, mBufferInfo);
                    prevOutputPTSUs = mBufferInfo.presentationTimeUs;
                }

                // return buffer to encoder
                mMediaCodec.releaseOutputBuffer(encoderStatus, false);
                if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    // when EOS come.
                    mIsCapturing = false;
                    break;      // out of while
                }
            }
        }
    }
    /**
     * previous presentationTimeUs for writing
     */
    private long prevOutputPTSUs = 0;
    /**
     * get next encoding presentationTimeUs
     * @return
     */
    protected long getPTSUs() {
        long result = System.nanoTime() / 1000L;
        // presentationTimeUs should be monotonic
        // otherwise muxer fail to write
        if (result < prevOutputPTSUs)
            result = (prevOutputPTSUs - result) + result;
        return result;
    }


}
