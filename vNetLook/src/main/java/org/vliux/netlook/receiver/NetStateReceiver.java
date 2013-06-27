package org.vliux.netlook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.vliux.netlook.util.NetUtil;

/**
 * Created by vliux on 6/27/13.
 */
public class NetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION) ||
            action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            int network = NetUtil.getConnectivityStatus(context);
            String msg = null;
            if(network == NetUtil.TYPE_MOBILE){
                msg = "[v] Using mobile 2G/3G network";
            }else if(network == NetUtil.TYPE_WIFI){
                msg = "[v] Using WIFI network";
            }else{
                msg = "[v] No connectivity";
            }
            Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
