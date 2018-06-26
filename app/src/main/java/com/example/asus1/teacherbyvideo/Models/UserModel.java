package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class UserModel {


    @SerializedName("id")
    private int mId;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("nickname")
    private String mNickname;

    @SerializedName("sex")
    private String mSex;

    @SerializedName("headportrait")
    private String mIcon;

    @SerializedName("phone")
    private String mPhone;

    @SerializedName("mail")
    private String mMail;

    @SerializedName("vip")
    private int mVip;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("birthday")
    private String mBirthday;

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmNickname() {
        return mNickname;
    }

    public void setmNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmMail() {
        return mMail;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public int getmVip() {
        return mVip;
    }

    public void setmVip(int mVip) {
        this.mVip = mVip;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmBirthday() {
        return mBirthday;
    }

    public void setmBirthday(String mBirthday) {
        this.mBirthday = mBirthday;
    }
}
