package org.vliux.netlook.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by vliux on 6/27/13.
 */
public class NetUtil {
    private static final String TAG = "NetUtil";
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NO_CONNECTION = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NO_CONNECTION;
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
        final Class connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
        final Field connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityService = connectivityManagerField.get(connectivityManager);
        final Class connectivityServiceClass = Class.forName(connectivityService.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityServiceClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityService, enabled);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            Log.e(TAG, "Class not found");
            throw new RuntimeException(e);
        }catch(NoSuchFieldException e){
            e.printStackTrace();
            Log.e(TAG, "No such field");
            throw new RuntimeException(e);
        }catch(IllegalAccessException e){
            e.printStackTrace();
            Log.e(TAG, "Illegal access");
            throw new RuntimeException(e);
        }catch(NoSuchMethodException e){
            e.printStackTrace();
            Log.e(TAG, "No such method");
            throw new RuntimeException(e);
        }catch(InvocationTargetException e){
            e.printStackTrace();
            Log.e(TAG, "Invokation taget exception");
            throw new RuntimeException(e);
        }
    }

}
