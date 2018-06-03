package com.example.asus1.teacherbyvideo.views.CameraGLViews;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.asus1.teacherbyvideo.Util.GLDrawer2D;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public final class CameraSurfaceRenderer implements GLSurfaceView.Renderer,
        SurfaceTexture.OnFrameAvailableListener{

    private static final String TAG = "CameraSurfaceRenderer";

    private CameraGLView mParent;
    public SurfaceTexture mSTexture;
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