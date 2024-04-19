package ru.mirea.mikhaylenkoma.serviceapp;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    private MediaPlayer _mediaPlayer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent _intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent _intent, int _flags, int _startId) {
        _mediaPlayer.start();
        _mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer _mp) {
                stopForeground(true);
            }
        });
        return super.onStartCommand(_intent, _flags, _startId);
    }

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder _builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Playing...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("The Mamas & the Papas"))
                .setContentText("California Dreamin'");
        int _importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel _channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            _channel = new NotificationChannel(CHANNEL_ID,
                    "Student Mikhaylenko Margarita Andreevna Notification", _importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _channel.setDescription("MIREA Channel");
        }
        NotificationManagerCompat _notificationManager = NotificationManagerCompat.from(this);
        _notificationManager.createNotificationChannel(_channel);
        startForeground(1, _builder.build());

        _mediaPlayer = MediaPlayer.create(this, R.raw.music);
        _mediaPlayer.setLooping(false);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        _mediaPlayer.stop();
    }
}