package org.vliux.netlook;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.vliux.netlook.model.AppNetUse;
import org.vliux.netlook.model.TotalNetUse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LoadNetUseAsyncTask extends AsyncTask<Void, Integer, TotalNetUse> {
    private static final String TAG = "LoadNetUseAsyncTask";
    public static final int MSG_COMPLETED = 1;
    public static final String[] NETWORK_PERMISSIONS = new String[]{
        "android.permission.INTERNET"
    };

    private PackageManager mPackageManager;
    private Handler mHandler;

    public LoadNetUseAsyncTask(PackageManager pkgManager, Handler handler){
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
            //if(rxBytes + txBytes <= 0L){
            //    Log.w(TAG, String.format(Locale.US, "Package %s ", pkg.packageName));
            //    continue;
            //}

            AppNetUse appUse = new AppNetUse();
            appUse.setmPackageName(pkg.packageName);
            try {
                appUse.setmIcon(mPackageManager.getApplicationIcon(pkg.packageName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                appUse.setmIcon(null);
            }
            appUse.setmRxBytes(rxBytes);
            appUse.setmTxBytes(txBytes);
            totalUse.getmAppNetUses().add(appUse);
            totalUse.setmRxBytes(totalUse.getmRxBytes() + rxBytes);
            totalUse.setmTxBytes(totalUse.getmTxBytes() + txBytes);
        }
        // ordering the collections according to network usage
        Collections.sort(totalUse.getmAppNetUses());
        Collections.reverse(totalUse.getmAppNetUses());
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
