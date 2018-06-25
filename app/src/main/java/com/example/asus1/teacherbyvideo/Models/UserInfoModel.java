package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class UserInfoModel {

    @SerializedName("sportcount")
    private int mSportCount;

    @SerializedName("usersporttime")
    private String mSportTime;

    @SerializedName("userscore")
    private int mUserScore;

    public int getSportCount() {
        return mSportCount;
    }

    public void setSportCount(int mSportCount) {
        this.mSportCount = mSportCount;
    }

    public String getSportTime() {
        return mSportTime;
    }

    public void setSportTime(String mSportTime) {
        this.mSportTime = mSportTime;
    }

    public int getUserScore() {
        return mUserScore;
    }

    public void setUserScore(int mUserScore) {
        this.mUserScore = mUserScore;
    }
}
