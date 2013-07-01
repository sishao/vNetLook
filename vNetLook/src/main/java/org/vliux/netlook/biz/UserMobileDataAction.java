package org.vliux.netlook.biz;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.vliux.netlook.db.DbManager;
import org.vliux.netlook.db.SettingsTable;
import org.vliux.netlook.model.SettingBean;

import java.util.Locale;

/**
 * Created by vliux on 7/1/13.
 */
public class UserMobileDataAction {
    private static final String TAG = "UserMobileDataAction";
    public static final String CFG_NAME = "isMonitorMobileData";
    public static final String[] CFG_VALUES = new String[]{"true", "false"};

    private SettingsTable mSettingsAdapter;
    private DbManager mDbManager;

    public UserMobileDataAction(Context context){
        mDbManager = new DbManager(context);
        mSettingsAdapter = mDbManager.getSettingsAdapter();
    }

    public void setMonitoring(boolean isOn){
        boolean res = false;
        if(null != mSettingsAdapter.getConfig(CFG_NAME)){
            ContentValues cv = new ContentValues();
            cv.put(SettingsTable.DB_COL_VALUE_TEXT_1, (isOn?CFG_VALUES[0]:CFG_VALUES[1]));
            res = mSettingsAdapter.updateConfig(CFG_NAME, cv);
        }else{
            SettingBean bean = new SettingBean(CFG_NAME, (isOn?CFG_VALUES[0]:CFG_VALUES[1]));
            res = mSettingsAdapter.addConfig(bean);
        }
        if(res){
            Log.i(TAG, String.format(Locale.US, "set mobile data monitoring %b OK", isOn));
        }else{
            Log.i(TAG, String.format(Locale.US, "set mobile data monitoring %b FAILED", isOn));
        }
    }

    public boolean getMonitoring(){
        SettingBean bean = mSettingsAdapter.getConfig(CFG_NAME);
        if(null == bean){
            return false;
        }else {
            return Boolean.valueOf(bean.getValue());
        }
    }

    public void close(){
        mDbManager.close();
    }
}
