package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class CourseDetailModel {

    @SerializedName("id")
    private int mId;

    @SerializedName("coursename")
    private String mCourseName;

    @SerializedName("coursetime")
    private long mCourseLegth;

    @SerializedName("selectedcount")
    private int mSelectedCount;

    @SerializedName("downloadcount")
    private int mDownLoadCount;

    @SerializedName("coursedesc")
    private String mType;

    @SerializedName("coursepic")
    private String mCover;

    @SerializedName("courselink")
    private String mCourseLink;

    @SerializedName("uploaduserphone")
    private String mUpLoadUser;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmCourseName() {
        return mCourseName;
    }

    public void setmCourseName(String mCourseName) {
        this.mCourseName = mCourseName;
    }

    public long getmCourseLegth() {
        return mCourseLegth;
    }

    public void setmCourseLegth(long mCourseLegth) {
        this.mCourseLegth = mCourseLegth;
    }

    public int getmSelectedCount() {
        return mSelectedCount;
    }

    public void setmSelectedCount(int mSelectedCount) {
        this.mSelectedCount = mSelectedCount;
    }

    public int getmDownLoadCount() {
        return mDownLoadCount;
    }

    public void setmDownLoadCount(int mDownLoadCount) {
        this.mDownLoadCount = mDownLoadCount;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmCover() {
        return mCover;
    }

    public void setmCover(String mCover) {
        this.mCover = mCover;
    }

    public String getmCourseLink() {
        return mCourseLink;
    }

    public void setmCourseLink(String mCourseLink) {
        this.mCourseLink = mCourseLink;
    }

    public String getmUpLoadUser() {
        return mUpLoadUser;
    }

    public void setmUpLoadUser(String mUpLoadUser) {
        this.mUpLoadUser = mUpLoadUser;
    }
}
