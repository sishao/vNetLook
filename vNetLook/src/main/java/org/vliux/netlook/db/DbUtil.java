package org.vliux.netlook.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vliux on 6/28/13.
 */
public class DbUtil {
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String dateToString(Date date){
        return sDateFormat.format(date);
    }

    public static Date stringToDate(String dateStr) throws ParseException{
        return sDateFormat.parse(dateStr);
    }
}
