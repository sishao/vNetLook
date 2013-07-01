package org.vliux.netlook.db;

import android.content.Context;

public class DbManager {
    private DbHelper mDbHelper;
    private AppNetUseTable mAppNetUseAdapter;
    private SettingsTable mSettingsAdapter;

    public DbManager(Context context){
        mDbHelper = new DbHelper(context);
        mAppNetUseAdapter = new AppNetUseTable(mDbHelper);
        mSettingsAdapter = new SettingsTable(mDbHelper);
    }

    public AppNetUseTable getAppNetUseAdapter(){
        return mAppNetUseAdapter;
    }

    public SettingsTable getSettingsAdapter(){
        return mSettingsAdapter;
    }

    public void close(){
        if(null != mDbHelper){
            mDbHelper.close();
        }
    }
}
