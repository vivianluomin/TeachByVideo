package com.example.asus1.teacherbyvideo.views;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.example.asus1.teacherbyvideo.Util.GLDrawer2D;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CameraGLView extends GLSurfaceView {

    private static final String TAG = "CameraGLView";

    private static final int CAMERA_ID = 1;

    private static final int SCALE_STRETCH_FIT = 0;
    private static final int SCALE_KEEP_ASPECT_VIEWPORT = 1;

    private final CameraSurfaceRenderer mRenderer;
    private boolean mHasSurface;
    private CmaeraHandler mCameraHandler = null;
    private int mVideoWidth, mVideoHeight;
    private int mRotation;
    private int mScaleMode = SCALE_STRETCH_FIT;

    public CameraGLView(Context context) {
        this(context,null);
    }

    public CameraGLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRenderer = new CameraSurfaceRenderer(this);
        setEGLContextClientVersion(2);	// GLES 2.0, API >= 8
        setRenderer(mRenderer);
    }

    @Override
    public void onResume() {

        super.onResume();
        Log.d(TAG, "onResume: ");
        if(mHasSurface){
            if(mCameraHandler == null){
                Log.d(TAG, "onResume: 222222222222");
                startPreview(getWidth(),  getHeight());
            }
        }
    }

    @Override
    public void onPause() {

        if (mCameraHandler != null) {
            // just request stop prviewing
            mCameraHandler.stopPreview(false);
        }
        super.onPause();
    }

    public void setVideoSize(final int width, final int height) {
        if ((mRotation % 180) == 0) {
            mVideoWidth = width;
            mVideoHeight = height;
        } else {
            mVideoWidth = height;
            mVideoHeight = width;
        }
//        queueEvent(new Runnable() {
//            @Override
//            public void run() {
//                mRenderer.updateViewport();
//            }
//        });
    }

    public SurfaceTexture getSurfaceTexture() {

        return mRenderer != null ? mRenderer.mSTexture : null;
    }

    @Override
    public void surfaceDestroyed(final SurfaceHolder holder) {

        if (mCameraHandler != null) {
            // wait for finish previewing here
            // otherwise camera try to display on un-exist Surface and some error will occure
            mCameraHandler.stopPreview(true);
        }
        mCameraHandler = null;
        mHasSurface = false;
        mRenderer.onSurfaceDestoryed();
        super.surfaceDestroyed(holder);
    }

    private synchronized void startPreview(final int width, final int height) {
        Log.d(TAG, "startPreview: ");
        if (mCameraHandler == null) {
            final CameraThread thread = new CameraThread(this);
            thread.start();
            mCameraHandler = thread.getHandler();
        }
        mCameraHandler.startPreview(1280, 720/*width, height*/);
    }



    private static final class CameraSurfaceRenderer implements GLSurfaceView.Renderer,
            SurfaceTexture.OnFrameAvailableListener{

        private CameraGLView mParent;
        private SurfaceTexture mSTexture;
        private int hTex;
        private GLDrawer2D mDrawer;
        private final float[] mStMatrix = new float[16];
        private final float[] mMvpMatrix = new float[16];

        public CameraSurfaceRenderer(CameraGLView parent) {
            mParent = parent;
            Matrix.setIdentityM(mMvpMatrix,0);

        }

        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
           // Log.d(TAG, "onFrameAvailable: ");
            requestUpdateTex = true;

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d(TAG, "onSurfaceCreated: ");
            hTex = GLDrawer2D.initTex();
            mSTexture = new SurfaceTexture(hTex);
            mSTexture.setOnFrameAvailableListener(this);

            GLES20.glClearColor(1.0f,1.0f,0.0f,1.0f);
            if (mParent != null) {
                mParent.mHasSurface = true;
            }
            mDrawer = new GLDrawer2D();
            mDrawer.setMatrix(mMvpMatrix,0);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.d(TAG, "onSurfaceChanged: ");
            if ((width == 0) || (height == 0)) return;
                GLES20.glViewport(0,0,width,height);
            if (mParent != null) {
                mParent.startPreview(width, height);
            }

        }

        public void onSurfaceDestoryed(){
            if(mDrawer!=null){
                mDrawer.release();
                mDrawer = null;
            }

            if(mSTexture!=null){
                mSTexture.release();
                mSTexture = null;
            }

            GLDrawer2D.deleteTex(hTex);
        }

        private volatile boolean requestUpdateTex = false;
        private boolean flip = true;

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            if(requestUpdateTex){
                requestUpdateTex = false;
                mSTexture.updateTexImage();
                mSTexture.getTransformMatrix(mStMatrix);
            }

            //Log.d(TAG, "onDrawFrame: "+mStMatrix.length);

            mDrawer.draw(hTex,mStMatrix);
        }
    }

    private static final class CmaeraHandler extends Handler{
        private static final int MSG_PREVIEW_START = 1;
        private static final int MSG_PREVIEW_STOP = 2;
        private CameraThread mThread;

        public CmaeraHandler(CameraThread thread){
            mThread = thread;
        }

        public void startPreview(int width,int height){
            sendMessage(obtainMessage(MSG_PREVIEW_START,width,height));
        }

        public void stopPreview(final boolean needWait) {
            synchronized (this) {
                sendEmptyMessage(MSG_PREVIEW_STOP);
                if (needWait && mThread.mIsRunning) {
                    try {
                        wait();
                    } catch (final InterruptedException e) {
                    }
                }
            }
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PREVIEW_START:
                    mThread.startPreview(msg.arg1, msg.arg2);
                    break;
                case MSG_PREVIEW_STOP:
                    mThread.stopPreview();
                    synchronized (this) {
                        notifyAll();
                    }
                    Looper.myLooper().quit();
                    mThread = null;
                    break;
                default:
                    throw new RuntimeException("unknown message:what=" + msg.what);
            }
        }
    }

    private static final class CameraThread extends Thread{
        private final Object mReadyFence = new Object();
        private final CameraGLView mParent;
        private CmaeraHandler mHandler;
        private volatile boolean mIsRunning = false;
        private Camera mCamers;
        private boolean mIsFrontFace;

        public CameraThread (CameraGLView parent){
            super("Camera thread");
            mParent = parent;

        }

        public CmaeraHandler getHandler(){
            synchronized (mReadyFence){
                try {
                    mReadyFence.wait();
                }catch (InterruptedException e){

                }
            }

            return mHandler;
        }

        @Override
        public void run() {
            synchronized (mReadyFence){
                Log.d(TAG, "run: ");
                mHandler = new CmaeraHandler(this);
                mIsRunning = true;
                mReadyFence.notify();
            }

            Looper.loop();
            synchronized (mReadyFence){
                mHandler = null;
                mIsRunning = false;
            }
        }

        private final void startPreview(int width,int height){
            Log.d(TAG, "startPreview: 11111111111111 ");
            if(mParent !=null && mCamers == null){
                try {
                    Log.d(TAG, "startPreview: 22222");
                    mCamers = Camera.open(CAMERA_ID);
                    Camera.Parameters parameters = mCamers.getParameters();
                    List<String> focusModes = parameters.getSupportedFocusModes();
                    if(focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)){
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                    }else if(focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)){
                        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }else {

                    }
                    final List<int[]> supportedFpsRange = parameters.getSupportedPreviewFpsRange();
                    final int[] max_fps = supportedFpsRange.get(supportedFpsRange.size() - 1);
                    Log.i(TAG, String.format("fps:%d-%d", max_fps[0], max_fps[1]));
                    parameters.setPreviewFpsRange(max_fps[0], max_fps[1]);
                    parameters.setRecordingHint(true);
                    // request closest supported preview size
                    final Camera.Size closestSize = getClosestSupportedSize(
                            parameters.getSupportedPreviewSizes(), width, height);
                    parameters.setPreviewSize(closestSize.width, closestSize.height);
                    // request closest picture size for an aspect ratio issue on Nexus7
                    final Camera.Size pictureSize = getClosestSupportedSize(
                            parameters.getSupportedPictureSizes(), width, height);
                    parameters.setPictureSize(pictureSize.width, pictureSize.height);
                    // rotate camera preview according to the device orientation
                    setRotation(parameters);
                    mCamers.setParameters(parameters);
                    // get the actual preview size
                    final Camera.Size previewSize = mCamers.getParameters().getPreviewSize();
                    Log.i(TAG, String.format("previewSize(%d, %d)", previewSize.width, previewSize.height));
                    // adjust view size with keeping the aspect ration of camera preview.
                    // here is not a UI thread and we should request parent view to execute.
                    mParent.post(new Runnable() {
                        @Override
                        public void run() {
                            mParent.setVideoSize(previewSize.width, previewSize.height);
                        }
                    });
                    final SurfaceTexture st = mParent.getSurfaceTexture();
                    st.setDefaultBufferSize(previewSize.width, previewSize.height);
                    mCamers.setPreviewTexture(st);
                }catch (IOException e){
                    if(mCamers!=null){
                        mCamers.release();
                        mCamers= null;
                    }
                }catch (RuntimeException e){
                    if(mCamers!=null){
                        mCamers.release();
                        mCamers= null;
                    }
                }

                if(mCamers!=null){
                    mCamers.startPreview();
                }

            }
        }

        private static Camera.Size getClosestSupportedSize(List<Camera.Size> supportedSizes, final int requestedWidth, final int requestedHeight) {
            return (Camera.Size) Collections.min(supportedSizes, new Comparator<Camera.Size>() {

                private int diff(final Camera.Size size) {
                    return Math.abs(requestedWidth - size.width) + Math.abs(requestedHeight - size.height);
                }

                @Override
                public int compare(final Camera.Size lhs, final Camera.Size rhs) {
                    return diff(lhs) - diff(rhs);
                }
            });

        }

        private void stopPreview() {
            if (mCamers != null) {
                mCamers.stopPreview();
                mCamers.release();
                mCamers = null;
            }
            if (mParent == null) return;
            mParent.mCameraHandler = null;
        }

        private final void setRotation(final Camera.Parameters params) {

            if (mParent == null) return;

            final Display display = ((WindowManager)mParent.getContext()
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            final int rotation = display.getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }
            // get whether the camera is front camera or back camera
            final Camera.CameraInfo info =
                    new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(CAMERA_ID, info);
            mIsFrontFace = (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
            if (mIsFrontFace) {	// front camera
                degrees = (info.orientation + degrees) % 360;
                degrees = (360 - degrees) % 360;  // reverse
            } else {  // back camera
                degrees = (info.orientation - degrees + 360) % 360;
            }
            // apply rotation setting
            mCamers.setDisplayOrientation(degrees);
            mParent.mRotation = degrees;
            // XXX This method fails to call and camera stops working on some devices.
//			params.setRotation(degrees);
        }
    }
}
