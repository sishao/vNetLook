package org.vliux.netlook.db;

import android.content.Context;

public class DbManager {
    private static DbManager instance;
    private static Context appContext;

    private DbHelper mDbHelper;
    private AppNetUseTable mAppNetUseAdapter;

    public synchronized static void setContext(Context context){
        appContext = context;
    }

    public synchronized static DbManager getInstance(){
        if(null != appContext){
            instance = new DbManager(appContext);
            return instance;
        }else{
            return null;
        }
    }

    private DbManager(Context context){
        mDbHelper = new DbHelper(context);
        mAppNetUseAdapter = new AppNetUseTable(mDbHelper);
    }

    public AppNetUseTable getAppNetUseAdapter(){
        return mAppNetUseAdapter;
    }
}
