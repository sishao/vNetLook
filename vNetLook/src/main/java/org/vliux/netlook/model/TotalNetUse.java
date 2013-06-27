package org.vliux.netlook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vliux on 6/26/13.
 */
public class TotalNetUse {
    private long mRxBytes = 0L;
    private long mTxBytes = 0L;
    private long mMobileRxBytes = 0L;
    private long mMobileTxBytes = 0L;
    private List<AppNetUse> mAppNetUses = new ArrayList<AppNetUse>();

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

    public List<AppNetUse> getmAppNetUses() {
        return mAppNetUses;
    }

    public void setmAppNetUses(List<AppNetUse> mAppNetUses) {
        this.mAppNetUses = mAppNetUses;
    }

    public long getmMobileRxBytes() {
        return mMobileRxBytes;
    }

    public void setmMobileRxBytes(long mMobileRxBytes) {
        this.mMobileRxBytes = mMobileRxBytes;
    }

    public long getmMobileTxBytes() {
        return mMobileTxBytes;
    }

    public void setmMobileTxBytes(long mMobileTxBytes) {
        this.mMobileTxBytes = mMobileTxBytes;
    }

    public long getMobileBytes(){
        return mMobileRxBytes + mMobileTxBytes;
    }
}
