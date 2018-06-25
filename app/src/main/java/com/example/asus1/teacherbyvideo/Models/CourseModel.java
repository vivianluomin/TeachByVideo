package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class CourseModel {

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

}
