package com.vamsigutha.rememb;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notification);
       String title = getString(R.string.dashLine)+"  "+intent.getStringExtra("title").trim();
       String CHANNEL_ID = intent.getStringExtra("CHANNEL_ID ");
       int BANNER_ID = intent.getIntExtra("BANNER_ID",27);

        Bitmap art = BitmapFactory.decodeResource(getResources(),R.drawable.logo);

        Intent landingIntent = new Intent(this, MainActivity.class);
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent landingPendingIntent = PendingIntent.getActivity(this,0,landingIntent,0);

        View v = LayoutInflater.from(this).inflate(remoteViews.getLayoutId(), null);
        remoteViews.setTextViewText(R.id.custom_text,title);
        if(BANNER_ID==27){
            remoteViews.setImageViewResource(R.id.banner,0);
        }else{
            remoteViews.setImageViewResource(R.id.banner,BANNER_ID);
            Log.e("from banner","from banner");
        }


//        remoteViews.setInt(v.getId(), "setBackgroundColor", Color.BLUE);
//        remoteViews.setInt(v.getId(), "setBackgroundResource",R.drawable.flower);



        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(landingPendingIntent)
                .setDefaults(0)
                .build();

        startForeground(1,notification);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
