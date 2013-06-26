package org.vliux.netlook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vliux on 6/26/13.
 */
public class TotalNetUse {
    private Long mRxBytes = new Long(0L);
    private Long mTxBytes = new Long(0L);
    private List<AppNetUse> mAppNetUses = new ArrayList<AppNetUse>();

    public Long getmRxBytes() {
        return mRxBytes;
    }

    public void setmRxBytes(Long mRxBytes) {
        this.mRxBytes = mRxBytes;
    }

    public Long getmTxBytes() {
        return mTxBytes;
    }

    public void setmTxBytes(Long mTxBytes) {
        this.mTxBytes = mTxBytes;
    }

    public List<AppNetUse> getmAppNetUses() {
        return mAppNetUses;
    }

    public void setmAppNetUses(List<AppNetUse> mAppNetUses) {
        this.mAppNetUses = mAppNetUses;
    }
}
