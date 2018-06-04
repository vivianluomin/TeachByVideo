package com.example.asus1.teacherbyvideo.Encoder;

import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.example.asus1.teacherbyvideo.Util.GLDrawer2D;

public class RenderHandler implements Runnable {

    private static final String TAG = "RenderHandler";

    private final Object mSync = new Object();
    private EGLContext mShard_context;
    private boolean mIsRecordable;
    private Object mSurface;
    private int mTexId = -1;
    private float[] mMatrix = new float[32];

    private boolean mRequestSetEglContext;
    private boolean mRequestRelease;
    private int mRequestDraw;

    public static final RenderHandler createHandler(final String name) {
        //if (DEBUG) Log.v(TAG, "createHandler:");
        final RenderHandler handler = new RenderHandler();
        synchronized (handler.mSync) {
            new Thread(handler, !TextUtils.isEmpty(name) ? name : TAG).start();
            try {
                handler.mSync.wait();
            } catch (final InterruptedException e) {
            }
        }
        return handler;
    }

    public final void setEglContext(EGLContext shared_context,int tex_id,
                                    Object surface,boolean isRecordable){
        if (!(surface instanceof Surface) && !(surface instanceof SurfaceTexture) && !(surface instanceof SurfaceHolder))
            throw new RuntimeException("unsupported window type:" + surface);

        synchronized (mSync){
            if(mRequestRelease) return;
            mShard_context = shared_context;
            mTexId = tex_id;
            mSurface = surface;
            mIsRecordable = isRecordable;
            mRequestSetEglContext = true;
            Matrix.setIdentityM(mMatrix, 0);
            Matrix.setIdentityM(mMatrix, 16);
            mSync.notifyAll();
            try {
                mSync.wait();
            } catch (final InterruptedException e) {
            }
        }
    }

    public final void draw() {
        draw(mTexId, mMatrix, null);
    }

    public final void draw(final int tex_id) {
        draw(tex_id, mMatrix, null);
    }

    public final void draw(final float[] tex_matrix) {
        draw(mTexId, tex_matrix, null);
    }

    public final void draw(final float[] tex_matrix, final float[] mvp_matrix) {
        draw(mTexId, tex_matrix, mvp_matrix);
    }

    public final void draw(final int tex_id, final float[] tex_matrix) {
        draw(tex_id, tex_matrix, null);
    }

    public final void draw(int tex_id,float[] tex_matrix,float[] mvp_matrix){
        synchronized (mSync){
            if(mRequestRelease) return;
            mTexId = tex_id;
            if(tex_matrix !=null && tex_matrix.length >=16){
                System.arraycopy(tex_matrix, 0, mMatrix, 0, 16);
        } else {
            Matrix.setIdentityM(mMatrix, 0);
        }

        mRequestDraw ++;
            mSync.notifyAll();
        }
    }

    public boolean isValid() {
        synchronized (mSync) {
            return !(mSurface instanceof Surface) || ((Surface)mSurface).isValid();
        }
    }

    public final void release() {
       // if (DEBUG) Log.i(TAG, "release:");
        synchronized (mSync) {
            if (mRequestRelease) return;
            mRequestRelease = true;
            mSync.notifyAll();
            try {
                mSync.wait();
            } catch (final InterruptedException e) {
            }
        }
    }

    private EGLBase mEgl;
    private EGLBase.EglSurface mInputSurface;
    private GLDrawer2D mDrawer;


    @Override
    public void run() {
        synchronized (mSync){
            mRequestSetEglContext = mRequestRelease = false;
            mRequestDraw = 0;
            mSync.notifyAll();
        }

        boolean localRequestDraw;
        for(;;){
            synchronized (mSync){
                if(mRequestRelease) break;
                if(mRequestSetEglContext){
                    mRequestSetEglContext = false;
                    internalPrepare();
                }

                localRequestDraw = mRequestDraw>0;
                if(localRequestDraw){
                    mRequestDraw--;
                }
            }

            if(localRequestDraw){
                if(mEgl == null &&mTexId >=0){
                    mInputSurface.makeCurrent();
                    GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                    mDrawer.setMatrix(mMatrix, 16);
                    mDrawer.draw(mTexId, mMatrix);
                    mInputSurface.swap();
                }
            }else {
                synchronized(mSync) {
                    try {
                        mSync.wait();
                    } catch (final InterruptedException e) {
                        break;
                    }
                }
            }
        }

        synchronized (mSync){
            mRequestRelease = true;
            internalRelease();
            mSync.notifyAll();
        }
    }

    private final void internalPrepare() {
        internalRelease();
        mEgl = new EGLBase(mShard_context, false, mIsRecordable);

        mInputSurface = mEgl.createFromSurface(mSurface);

        mInputSurface.makeCurrent();
        mDrawer = new GLDrawer2D();
        mSurface = null;
        mSync.notifyAll();
    }

    private final void internalRelease() {

        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (mDrawer != null) {
            mDrawer.release();
            mDrawer = null;
        }
        if (mEgl != null) {
            mEgl.release();
            mEgl = null;
        }
    }
}
