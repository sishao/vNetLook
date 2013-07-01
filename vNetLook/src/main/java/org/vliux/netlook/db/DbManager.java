package org.vliux.netlook.db;

import android.content.Context;

public class DbManager {
    private DbHelper mDbHelper;
    private AppNetUseTable mAppNetUseAdapter;

    public DbManager(Context context){
        mDbHelper = new DbHelper(context);
        mAppNetUseAdapter = new AppNetUseTable(mDbHelper);
    }

    public AppNetUseTable getAppNetUseAdapter(){
        return mAppNetUseAdapter;
    }

    public void close(){
        if(null != mDbHelper){
            mDbHelper.close();
        }
    }
}
