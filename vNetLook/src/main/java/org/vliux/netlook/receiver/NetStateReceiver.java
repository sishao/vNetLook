package org.vliux.netlook.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import org.vliux.netlook.R;
import org.vliux.netlook.util.NetUtil;
import org.vliux.netlook.util.NotifyUtil;

public class NetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            String msg = null;
            switch(state){
                case WifiManager.WIFI_STATE_DISABLED:
                    msg = "WIFI is disabled";
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    msg = "WIFI is disabling ...";
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    msg = "WIFI is enabled";
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    msg = "WIFI is enabling ...";
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    msg = "[ERROR] WIFI state is unknown";
                    break;
            }
            if(null != msg){
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }else if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(null != networkInfo &&
                    networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                String msg = null;
                switch(networkInfo.getState()){
                    case DISCONNECTED:
                        msg = "Mobile 2G/3G is disabled";
                        break;
                    case DISCONNECTING:
                        msg = "Mobile 2G/3G disabling ...";
                        break;
                    case CONNECTED:
                        msg = "Mobile 2G/3G is enabled";
                        NotifyUtil.sendNotification(context, "Mobile 2G/3G is enabled", "Now you are using mobile 2G/3G network",
                                -1, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
                        break;
                    case CONNECTING:
                        msg = "Mobile 2G/3G is enabling ...";
                        NotifyUtil.sendNotification(context, "Mobile 2G/3G is enabling ...", "Now you will use mobile 2G/3G network",
                                -1, BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
                        break;
                }
                if(null != msg){
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}
