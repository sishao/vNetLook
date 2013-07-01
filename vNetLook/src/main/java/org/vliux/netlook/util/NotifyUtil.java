package org.vliux.netlook.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.BitSet;

/**
 * Created by vliux on 7/1/13.
 */
public class NotifyUtil {
    public static void sendNotification(Context context, String title, String text, int smallIcon, Bitmap largeIcon){
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text);
        if(smallIcon > 0){
            builder.setSmallIcon(smallIcon);
        }
        if(null != largeIcon){
            builder.setLargeIcon(largeIcon);
        }

        Uri ringtoneUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        builder.setSound(ringtoneUrl);

        Notification noti = builder.getNotification();
        NotificationManager notifyManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.notify(1, noti);
    }
}
