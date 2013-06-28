package org.vliux.netlook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DB_VER = 1;
    public static final String DB_NAME = "vnetlook.db";

    private List<DbTable> mTables;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
        mTables = new ArrayList<DbTable>();
    }

    public void registerDbTable(DbTable dbTable){
        mTables.add(dbTable);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for(DbTable table : mTables){
            String sql = table.getCreateSql();
            if(null != sql && sql.length() > 0){
                sqLiteDatabase.execSQL(sql);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        for(DbTable table : mTables){
            String sql = table.getUpdateSql(oldVer, newVer);
            if(null != sql && sql.length() > 0){
                sqLiteDatabase.execSQL(sql);
            }
        }
    }

}
