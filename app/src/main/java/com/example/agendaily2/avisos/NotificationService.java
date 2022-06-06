package com.example.agendaily2.avisos;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.example.agendaily2.activitys.Menus;
import com.example.agendaily2.R;

public class NotificationService extends IntentService {

    //Creamos paramatros para la notificación
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;

    private static int NOTIFICATION_ID = 1;
    Notification notification;



    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("SERVICE");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent2) {

        //Asiganmos un nombre para la notificación
        String NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name);
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Creamos un intent para cuando clickemos sobre la notifcación nos lleve a los menus de la app
        Intent mIntent = new Intent(this, Menus.class);
        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);


//Mnesaje que saldra en la notificación
        String message = getString(R.string.new_notificacion);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            final int NOTIFY_ID = 0; // ID de la notificación
            String id = NOTIFICATION_CHANNEL_ID; // id del canal por defecto
            String title = NOTIFICATION_CHANNEL_ID; // titulo del canal por defecto
            PendingIntent pendingIntent;
            NotificationCompat.Builder builder;
            //Construimos la notificació
            NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notifManager == null) {
                notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            //Asignamos los ajustes de la notificacion , su id el titulo la importancia y la vibracion
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }


            builder = new NotificationCompat.Builder(context, id);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Agregamos un titulo a la notificación
            builder.setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                    //agregamos un logo
                    .setSmallIcon(R.drawable.logoapp)
                    //Monstramos el mensaje
                    .setContentText(message)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logoapp))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(soundUri)

                    //Establecemos la vibración
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);

            startForeground(1, notification);

        } else {
            pendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logoapp)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logoapp))
                    .setSound(soundUri)
                    .setAutoCancel(true)
                    .setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                    .setContentText(message).build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}