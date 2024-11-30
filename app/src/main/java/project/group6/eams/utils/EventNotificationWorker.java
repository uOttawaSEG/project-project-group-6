package project.group6.eams.utils;

import static android.icu.number.NumberFormatter.with;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Date;

import project.group6.eams.R;
import project.group6.eams.activities.LoginPage;
import project.group6.eams.users.*;

public class EventNotificationWorker extends Worker {

    public EventNotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Data eventInfo = getInputData();
        String eventName = eventInfo.getString("eventName");
        long startTime = eventInfo.getLong("startTime",0);
        AppInfo appInfo = AppInfo.getInstance();

        // time handling to make sure notification is sent 24 hours before event
        long currentTime = System.currentTimeMillis();
        long timeOneDayBefore = startTime - 24 * 60 * 60 * 1000;


        User currentUser = appInfo.getCurrentUser();
        if (currentUser == null) {
            Log.e("EventNotificationWorker", "No current user found");
            return Result.failure();
        } else if (currentUser.getUserType().equals("Attendee")) {

            if (currentTime >= timeOneDayBefore) { // within a 24 hour period
                sendReminder(eventName);
                Log.i("EventNotificationWorker", "Reminder sent for event: " + eventName);
            }

        }
        return Result.success();
    }

    /**
     * Reminder is sent to Attendee that is logged in.
     *
     * @param eventName event that is coming up in 24 hours.
     */
    private void sendReminder(String eventName) {
        Log.d("EventNotificationWorker", "Sending reminder for event: " + eventName);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), "event_reminders")
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle(eventName + " starts soon!")
                .setContentText("Dont forget, Event '" + eventName + "' starts in 24 hours or less! Come check it's start time.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // i dont really want to handle this rn so imma just assume they give permissions... oops
            Log.e("EventNotificationWorker", "No permission to send notifications");
            return;
        }
        notificationManager.notify(0, builder.build());
    }
}
