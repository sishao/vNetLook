package org.vliux.netlook.db;

import android.content.ContentValues;
import android.database.Cursor;

import org.vliux.netlook.model.AppNetUse;
import org.vliux.netlook.model.SettingBean;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by vliux on 7/1/13.
 */
public class SettingsTable extends DbTable {
    public static final String DB_COL_NAME_TEXT_1 = "settingName";
    public static final String DB_COL_VALUE_TEXT_1 = "settingValue";

    protected SettingsTable(DbHelper helper) {
        super(helper);
    }

    public SettingBean getConfig(String settingName){
        SettingBean settingBean = null;
        String[] columns = new String[]{DB_COL_NAME_TEXT_1, DB_COL_VALUE_TEXT_1};
        ContentValues cv = new ContentValues();
        cv.put(DB_COL_NAME_TEXT_1, settingName);
        Cursor cur = __select(columns, cv, null);
        if(null == cur){
            return null;
        }

        try{
            for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()){
                settingBean = new SettingBean(cur.getString(0), cur.getString(1));
                return settingBean;
            }
        }finally {
            if(null != cur){
                cur.close();
            }
        }
        return null;
    }

    public boolean updateConfig(String settingName, ContentValues newValues){
        return update(newValues,
                String.format(Locale.US, "%s=?", DB_COL_NAME_TEXT_1),
                new String[]{settingName});
    }

    public boolean addConfig(SettingBean setting){
        ContentValues cv = new ContentValues();
        cv.put(DB_COL_NAME_TEXT_1, setting.getName());
        cv.put(DB_COL_VALUE_TEXT_1, setting.getValue().toString());
        return insert(cv);
    }
}
