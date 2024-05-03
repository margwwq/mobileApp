package ru.mirea.mikhaylenkoma.mireaproject.ui.les4;

import static android.Manifest.permission.POST_NOTIFICATIONS;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import ru.mirea.mikhaylenkoma.mireaproject.R;

public class NotificationWorker extends Worker {
    private final String TAG = this.getClass().getSimpleName();
    private static final String CHANNEL_ID = "ID";
    private Context _thisContext;

    public NotificationWorker(@NonNull Context _context, @NonNull WorkerParameters _params) {
        super(_context, _params);
        _thisContext = _context;
    }

    @Override
    public Result doWork() {
        String _title = getInputData().getString("Title");
        String _description = getInputData().getString("Description");

        ShowNotification(_title, _description);

        return Result.success();
    }

    private void ShowNotification(String _title, String _description) {
        if(ActivityCompat.checkSelfPermission(_thisContext, POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationCompat.Builder _builder = new NotificationCompat.Builder(_thisContext, CHANNEL_ID)
                .setContentTitle(_title)
                .setContentText(_description)
                .setSmallIcon(R.drawable.notification_icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        int _importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel _channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _channel = new NotificationChannel(CHANNEL_ID, "WORKER", _importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _channel.setDescription(_description);
        }
        NotificationManagerCompat _notificationManagerCompat = NotificationManagerCompat.from(_thisContext);
        _notificationManagerCompat.createNotificationChannel(_channel);
        _notificationManagerCompat.notify(1, _builder.build());
    }
}