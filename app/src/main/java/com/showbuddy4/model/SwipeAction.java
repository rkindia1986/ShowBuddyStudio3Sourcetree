package com.showbuddy4.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jainam on 14-12-2017.
 */

public class SwipeAction {
    @Override
    public String toString() {
        return "SwipeAction{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    @SerializedName("status")
    private String status;
    @SerializedName("msg")
    private String msg;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
