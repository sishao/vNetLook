package org.vliux.netlook.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

public class AppNetUse {
    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public Drawable getmIcon() {
        return mIcon;
    }

    public void setmIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public long getmRxBytes() {
        return mRxBytes;
    }

    public void setmRxBytes(long mRxBytes) {
        this.mRxBytes = mRxBytes;
    }

    public long getmTxBytes() {
        return mTxBytes;
    }

    public void setmTxBytes(long mTxBytes) {
        this.mTxBytes = mTxBytes;
    }

    public Date getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(Date mStartTime) {
        this.mStartTime = mStartTime;
    }

    private String mAppName;
    private String mPackageName;
    private Drawable mIcon;
    private long mRxBytes;
    private long mTxBytes;
    private Date mStartTime;

}
