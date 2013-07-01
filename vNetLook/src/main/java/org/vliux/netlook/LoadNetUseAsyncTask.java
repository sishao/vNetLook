package org.vliux.netlook;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.vliux.netlook.db.AppNetUseTable;
import org.vliux.netlook.db.DbManager;
import org.vliux.netlook.db.DbUtil;
import org.vliux.netlook.model.AppNetUse;
import org.vliux.netlook.model.TotalNetUse;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoadNetUseAsyncTask extends AsyncTask<Void, Integer, TotalNetUse> {
    private static final String TAG = "LoadNetUseAsyncTask";
    public static final int MSG_COMPLETED = 1;
    public static final String[] NETWORK_PERMISSIONS = new String[]{
        "android.permission.INTERNET"
    };

    private DbManager mDbManager;
    private PackageManager mPackageManager;
    private Handler mHandler;

    public LoadNetUseAsyncTask(DbManager dbManager, PackageManager pkgManager, Handler handler){
        mDbManager = dbManager;
        mPackageManager = pkgManager;
        mHandler = handler;
    }

    private boolean hasNetworkPermission(PackageInfo pkgInfo){
        if(null == pkgInfo || null == pkgInfo.requestedPermissions){
            return false;
        }

        for(String perm : pkgInfo.requestedPermissions){
            for(String p : NETWORK_PERMISSIONS){
                if(p.equals(perm)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected TotalNetUse doInBackground(Void... voids) {
        TotalNetUse totalUse = new TotalNetUse();
        if(null == mPackageManager){
            return totalUse;
        }
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(PackageManager.GET_META_DATA | PackageManager.GET_PERMISSIONS);
        for(PackageInfo pkg : packages){
            if(!hasNetworkPermission(pkg)){
                Log.w(TAG, String.format(Locale.US, "Package %s doesn't have network permission", pkg.packageName));
                continue;
            }

            long rxBytes = TrafficStats.getUidRxBytes(pkg.applicationInfo.uid);
            long txBytes = TrafficStats.getUidTxBytes(pkg.applicationInfo.uid);

            AppNetUse appUse = new AppNetUse();
            appUse.setmPackageName(pkg.packageName);
            appUse.setLabel(mPackageManager.getApplicationLabel(pkg.applicationInfo).toString());
            appUse.setmStartTime(new Date());
            try {
                appUse.setmIcon(mPackageManager.getApplicationIcon(pkg.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                appUse.setmIcon(null);
            }
            appUse.setmRxBytes(rxBytes);
            appUse.setmTxBytes(txBytes);
            totalUse.getmAppNetUses().add(appUse);
            // save to db
            AppNetUseTable appNetUseAdapter = mDbManager.getAppNetUseAdapter();
            if (null == appNetUseAdapter.get(appUse.getmPackageName())) {
                appNetUseAdapter.add(appUse);
            } else {
                ContentValues cv = new ContentValues();
                cv.put(AppNetUseTable.DB_COL_LABEL_TEXT_1, appUse.getLabel());
                cv.put(AppNetUseTable.DB_COL_RX_INTEGER_1, appUse.getmRxBytes());
                cv.put(AppNetUseTable.DB_COL_TX_INTEGER_1, appUse.getmTxBytes());
                cv.put(AppNetUseTable.DB_COL_LAST_TIME_TEXT_1, DbUtil.dateToString(appUse.getmStartTime()));
                appNetUseAdapter.update(appUse.getmPackageName(), cv);
            }
        }
        totalUse.setmTxBytes(TrafficStats.getTotalTxBytes());
        totalUse.setmRxBytes(TrafficStats.getTotalRxBytes());
        totalUse.setmMobileTxBytes(TrafficStats.getMobileTxBytes());
        totalUse.setmMobileRxBytes(TrafficStats.getMobileRxBytes());
        // ordering the collections according to network usage
        Collections.sort(totalUse.getmAppNetUses());
        Collections.reverse(totalUse.getmAppNetUses());
        mDbManager.close();
        return totalUse;
    }

    @Override
    protected void onPostExecute(TotalNetUse totalNetUse) {
        super.onPostExecute(totalNetUse);
        if(null != mHandler){
            Message msg = mHandler.obtainMessage(MSG_COMPLETED);
            msg.obj = totalNetUse;
            mHandler.sendMessage(msg);
        }
    }
}
