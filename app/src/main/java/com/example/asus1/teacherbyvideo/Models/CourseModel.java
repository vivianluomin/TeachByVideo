package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CourseModel implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("coursename")
    private String mCourseName;

    @SerializedName("selectedcount")
    private int mSelectedCount;

    @SerializedName("downloadcount")
    private int mLoadDownCount;

    @SerializedName("coursedesc")
    private String mIntroduction;

    @SerializedName("coursepic")
    private String mCourseCover;

    @SerializedName("courselink")
    private String mCourseLink;

    @SerializedName("uploaduserphone")
    private String mUploadUserPhone;


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

    public int getmSelectedCount() {
        return mSelectedCount;
    }

    public void setmSelectedCount(int mSelectedCount) {
        this.mSelectedCount = mSelectedCount;
    }

    public int getmLoadDownCount() {
        return mLoadDownCount;
    }

    public void setmLoadDownCount(int mLoadDownCount) {
        this.mLoadDownCount = mLoadDownCount;
    }

    public String getmIntroduction() {
        return mIntroduction;
    }

    public void setmIntroduction(String mIntroduction) {
        this.mIntroduction = mIntroduction;
    }

    public String getmCourseCover() {
        return mCourseCover;
    }

    public void setmCourseCover(String mCourseCover) {
        this.mCourseCover = mCourseCover;
    }

    public String getmCourseLink() {
        return mCourseLink;
    }

    public void setmCourseLink(String mCourseLink) {
        this.mCourseLink = mCourseLink;
    }

    public String getmUploadUserPhone() {
        return mUploadUserPhone;
    }

    public void setmUploadUserPhone(String mUploadUserPhone) {
        this.mUploadUserPhone = mUploadUserPhone;
    }
}
