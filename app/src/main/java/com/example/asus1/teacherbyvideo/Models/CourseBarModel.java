package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class CourseBarModel {

    @SerializedName("id")
    private int mId;

    @SerializedName("courseid")
    private int mCourseId;

    @SerializedName("number")
    private int mNumber;

    @SerializedName("courseurl")
    private String mCourseLink;

    @SerializedName("coursepic")
    private String mCourseCover;

    @SerializedName("coursename")
    private String mCourseBarName;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getCourseId() {
        return mCourseId;
    }

    public void setCourseId(int mCourseId) {
        this.mCourseId = mCourseId;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int mNumber) {
        this.mNumber = mNumber;
    }

    public String getCourseLink() {
        return mCourseLink;
    }

    public void setCourseLink(String mCourseLink) {
        this.mCourseLink = mCourseLink;
    }

    public String getCourseCover() {
        return mCourseCover;
    }

    public void setCourseCover(String mCourseCover) {
        this.mCourseCover = mCourseCover;
    }

    public String getCourseBarName() {
        return mCourseBarName;
    }

    public void setCourseBarName(String mCourseBarName) {
        this.mCourseBarName = mCourseBarName;
    }
}
