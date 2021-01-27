package com.parkch.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
    AnimationDrawable roketAnimation;

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel description");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        ImageView roketImageView = (ImageView)findViewById(R.id.roket_Image);
        roketImageView.setBackgroundResource(R.drawable.roket);
        roketAnimation = (AnimationDrawable)roketImageView.getBackground();
    }

    public void sendNotification(View view) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        // 알림이 클릭되면 이 인텐트가 보내진다.
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        notificationBuilder.setSmallIcon(R.drawable.android)
                .setContentTitle("메일 알림")
                .setContentText("새로운 메일이 도착하였습니다.")
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            roketAnimation.start();
            return true;
        }
        return  super.onTouchEvent(event);
    }
}
