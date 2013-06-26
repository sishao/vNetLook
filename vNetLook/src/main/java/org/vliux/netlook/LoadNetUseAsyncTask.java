package org.vliux.netlook;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import org.vliux.netlook.model.AppNetUse;
import org.vliux.netlook.model.TotalNetUse;

import java.util.List;

public class LoadNetUseAsyncTask extends AsyncTask<Void, Integer, TotalNetUse> {
    public static final int MSG_COMPLETED = 1;

    private PackageManager mPackageManager;
    private Handler mHandler;

    public LoadNetUseAsyncTask(PackageManager pkgManager, Handler handler){
        mPackageManager = pkgManager;
        mHandler = handler;
    }

    @Override
    protected TotalNetUse doInBackground(Void... voids) {
        TotalNetUse totalUse = new TotalNetUse();
        if(null == mPackageManager){
            return totalUse;
        }
        List<PackageInfo> packages = mPackageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        for(PackageInfo pkg : packages){
            long rxBytes = TrafficStats.getUidRxBytes(pkg.applicationInfo.uid);
            long txBytes = TrafficStats.getUidTxBytes(pkg.applicationInfo.uid);
            if(rxBytes + txBytes <= 0L){
                continue;
            }

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
