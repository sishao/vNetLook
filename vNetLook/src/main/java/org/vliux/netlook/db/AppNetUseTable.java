package org.vliux.netlook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.vliux.netlook.model.AppNetUse;

import java.text.ParseException;
import java.util.Locale;

public class AppNetUseTable extends DbTable {
    public static final String DB_COL_PKG_TEXT_1 = "packageName";
    public static final String DB_COL_LABEL_TEXT_1 = "appLabel";
    public static final String DB_COL_RX_INTEGER_1 = "rxBytes";
    public static final String DB_COL_TX_INTEGER_1 = "txBytes";
    public static final String DB_COL_LAST_TIME_TEXT_1 = "lastSampleTime";

    public AppNetUseTable(Context context){
        super(context);
    }

    public boolean add(AppNetUse anu){
        if(null == anu){
            throw new IllegalArgumentException();
        }
        ContentValues cv = new ContentValues();
        cv.put(DB_COL_LABEL_TEXT_1, anu.getLabel());
        cv.put(DB_COL_PKG_TEXT_1, anu.getmPackageName());
        cv.put(DB_COL_RX_INTEGER_1, anu.getmRxBytes());
        cv.put(DB_COL_TX_INTEGER_1, anu.getmTxBytes());
        cv.put(DB_COL_LAST_TIME_TEXT_1, DbUtil.dateToString(anu.getmStartTime()));
        return insert(cv);
    }

    public AppNetUse get(String packageName){
        AppNetUse netUse = null;
        String[] columns = new String[]{DB_COL_LABEL_TEXT_1, DB_COL_PKG_TEXT_1,
            DB_COL_RX_INTEGER_1, DB_COL_TX_INTEGER_1, DB_COL_LAST_TIME_TEXT_1};
        ContentValues cv = new ContentValues();
        cv.put(DB_COL_PKG_TEXT_1, packageName);
        Cursor cur = __select(columns, cv, null);
        if(null == cur){
            return null;
        }

        try{
            for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
                netUse = new AppNetUse();
                netUse.setmPackageName(cur.getString(1));
                netUse.setLabel(cur.getString(0));
                netUse.setmRxBytes(cur.getLong(2));
                netUse.setmTxBytes(cur.getLong(3));
                try{
                    netUse.setmStartTime(DbUtil.stringToDate(cur.getString(4)));
                }catch(ParseException e){
                    e.printStackTrace();
                    netUse.setmStartTime(null);
                }
                return netUse;
            }
        }finally {
            if(null != cur){
                cur.close();
            }
        }
        return null;
    }

    public boolean update(String packageName, ContentValues newValues){
        if(null == packageName || packageName.length() <= 0){
            throw new IllegalArgumentException();
        }
        if(null == newValues){
            throw new IllegalArgumentException();
        }
        return update(newValues,
                String.format(Locale.US, "%s=?", DB_COL_PKG_TEXT_1),
                new String[]{packageName});

    }

}
