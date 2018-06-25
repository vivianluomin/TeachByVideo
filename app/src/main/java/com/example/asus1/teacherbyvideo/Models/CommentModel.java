package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class CommentModel {

    @SerializedName("id")
    private int mId;

    @SerializedName("courseid")
    private int mCourseId;

    @SerializedName("pingjia")
    private String mComment;

    @SerializedName("time")
    private String mDate;

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

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }
}
