package com.elegion.tracktor.service;
/*
Всю логику, связанную с созданием уведомления
переносим из CounterService
в отдельный класс - NotificationHelper
 */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.elegion.tracktor.R;
import com.elegion.tracktor.ui.map.MainActivity;

public abstract class NotificationHelper extends Service {

    //https://www.google.com/search?q=notification+helper+android+%D0%BF%D1%80%D0%B8%D0%BC%D0%B5%D1%80&rlz=1C1GCEU_ruRU836RU837&sxsrf=AOaemvJjTqdcasvKMGq3seoWaE9ppPQclg%3A1632111730671&ei=cgxIYeGuKObKrgT0j6TADQ&oq=notification+helper+android+ghbvth&gs_lcp=Cgdnd3Mtd2l6EAEYADIHCCEQChCgAToHCAAQRxCwAzoGCAAQFhAeOgUIIRCgAUoECEEYAFCoMFi8YmDnamgBcAJ4AIABuAGIAbkJkgEDMC43mAEAoAEByAEIwAEB&sclient=gws-wiz
    //https://habr.com/ru/post/244423/

    // https://github.com/googlearchive/android-NotificationChannels/blob/master/Application/src/main/java/com/example/android/notificationchannels/NotificationHelper.java

    private static Context appContext; // контекст приложения

    public static final String CHANNEL_ID = "counter_service";
    public static final String CHANNEL_NAME = "Counter Service";
    public static final int NOTIFICATION_ID = 101;
    public static final int REQUEST_CODE_LAUNCH = 0;

    private NotificationCompat.Builder mNotificationBuilder;
    private static NotificationManager mNotificationManager; // менеджер уведомлений

    // метод инциализации данного хелпера
    public static void init(Context context){
        if(mNotificationManager ==null){
            appContext = context.getApplicationContext(); // на случай инициализации Base Context-ом
            mNotificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }




    public void createNotification(){
        //создаем все необходимое для нотификации

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        Notification notification = buildNotification();
        startForeground(NOTIFICATION_ID, notification);


    }
    private Notification buildNotification() {
        return buildNotification("", "");
    }

    private Notification buildNotification(String time, String distance) {
        if (mNotificationBuilder == null) {
            configureNotificationBuilder();
        }

        String message = getString(R.string.notify_info, time, distance);

        return mNotificationBuilder
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();

    }

    private void configureNotificationBuilder() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, REQUEST_CODE_LAUNCH, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_white_24dp)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.route_active))
                .setVibrate(new long[]{0})
                .setColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (mNotificationManager != null && mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            mNotificationManager.createNotificationChannel(chan);
        }
    }


}
