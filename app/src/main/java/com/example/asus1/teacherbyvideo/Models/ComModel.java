package com.example.asus1.teacherbyvideo.Models;

import com.google.gson.annotations.SerializedName;

public class ComModel<T> {

    @SerializedName("Status")
    private int mStatus;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("data")
    private T mData;

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public T getmData() {
        return mData;
    }

    public void setmData(T mData) {
        this.mData = mData;
    }
}
