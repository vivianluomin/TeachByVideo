package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class DownLoadCourseModel {

    @SerializedName("id")
    private int mId;

    @SerializedName("coursename")
    private String mCourseName;

    @SerializedName("coursename")
    private long mTime;

    @SerializedName("selectedcount")
    private int mSelectedCount;

    @SerializedName("downloadcount")
    private int mDownloadCount;

    @SerializedName("coursedesc")
    private String mType;

    @SerializedName("courselink")
    private String mCourseLink;

    @SerializedName("coursepic")
    private String mCover;

    @SerializedName("uploaduser")
    private String mUploadUser;


    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getCourseName() {
        return mCourseName;
    }

    public void setCourseName(String mCourseName) {
        this.mCourseName = mCourseName;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long mTime) {
        this.mTime = mTime;
    }

    public int getSelectedCount() {
        return mSelectedCount;
    }

    public void setSelectedCount(int mSelectedCount) {
        this.mSelectedCount = mSelectedCount;
    }

    public int getDownloadCount() {
        return mDownloadCount;
    }

    public void setDownloadCount(int mDownloadCount) {
        this.mDownloadCount = mDownloadCount;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getCourseLink() {
        return mCourseLink;
    }

    public void setCourseLink(String mCourseLink) {
        this.mCourseLink = mCourseLink;
    }

    public String getCover() {
        return mCover;
    }

    public void setCover(String mCover) {
        this.mCover = mCover;
    }

    public String getUploadUser() {
        return mUploadUser;
    }

    public void setUploadUser(String mUploadUser) {
        this.mUploadUser = mUploadUser;
    }
}
