package com.drcita.user.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;


import com.drcita.user.DashBoardActivity;
import com.drcita.user.R;
import com.drcita.user.common.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private String userId;
    private String trainerId;
    private String studentId;
    private String userType;
    private String userTypeDB;
    private String postId;
    private String classId;
    private String sectionId;
    private String date;
    Context context;

//    private SharedPreferences preferences;

    private String TAG = "MyFireBaseMessagingService";
    private Bitmap icon;

    @Override
    public void onNewToken(@NotNull String s) {
        super.onNewToken(s);

        Log.e(TAG, "onNewToken: " + s);
//        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
//        userId = preferences.getString(Constants.USER_ID, userId);
//        userTypeDB = preferences.getString(Constants.USER_TYPE, userTypeDB);
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        context = this;
        /*title' =>$title,
        'body' =>$msg,
                'type' => $type,
                'sound' => "default",
                'student_id' =>$student_id,
                'class_id' => $class_id,
                'section_id' => $section_id,
                'teacher_id' => $teacher_id,
                'parent_id' => $parent_id,
                'admin_id' => $admin_id,
                'date' => $date,*/
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String type = remoteMessage.getData().get("type");
        trainerId = remoteMessage.getData().get("teacher_id");
        studentId = remoteMessage.getData().get("student_id");
        userType = remoteMessage.getData().get("user_type");
        postId = remoteMessage.getData().get("post_id");
        classId = remoteMessage.getData().get("class_id");
        sectionId = remoteMessage.getData().get("section_id");
        date = remoteMessage.getData().get("date");
        Log.e(TAG, "message: " + remoteMessage.getData().toString());
        Log.e(TAG, "onMessageReceived: " + title + "," + body + "," + type + "," + trainerId + "  ,  " + studentId + "  ,  " + userType + "   ,   " + postId + "," + classId + "," + sectionId + date);
        sendHomeWork(2, title, body, classId, sectionId, userType, date, remoteMessage.getData().get("image"));

    }

    private void sendHomeWork(int type, String title, String body, String classId, String sectionId, String userType, String date, String image) {
        long notificationId = System.currentTimeMillis();
        Intent intent;

        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        userId = sp.getString(Constants.USER_ID, userId);

        Log.e(TAG, "sendNotification: 00    " + userId + "   ,   " + postId);
        intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        intent.putExtra("notification",true);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);
        NotificationManager mNotificationManage =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            Objects.requireNonNull(mNotificationManage).createNotificationChannel(channel);
        }

        icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_foreground);

        if (image.isEmpty()) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "default")
                    .setLargeIcon(icon)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setContentTitle(this.getResources().getString(R.string.app_name))
                    .setContentTitle(title)
                    .setContentText(body)
                    // .setStyle(new Notification.BigPictureStyle().bigPicture(image))
                    // .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentIntent(contentIntent);


            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(mNotificationManager).notify((int) notificationId, notificationBuilder.build());
        } else {
            final Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(context)
                            .load(image)
                            .resize(200, 200)
                            .into(new Target() {
                                private Bitmap bitmap;

                                @Override
                                public void onBitmapLoaded(final Bitmap bitmap, final Picasso.LoadedFrom from) {
                                    this.bitmap = bitmap;
                                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "default")
                                            .setLargeIcon(icon)
                                            .setSmallIcon(R.mipmap.ic_launcher_foreground)
                                            .setContentTitle(context.getResources().getString(R.string.app_name))
                                            .setContentTitle(title)
                                            .setContentText(body)
                                            // .setStyle(new Notification.BigPictureStyle().bigPicture(image))
                                            // .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                                            .setAutoCancel(true)
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                                            .setContentIntent(contentIntent);


                                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(this.bitmap).setSummaryText(body).setSummaryText(body));
                                    Objects.requireNonNull(mNotificationManager).notify((int) notificationId, notificationBuilder.build());
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable) {

                                }

                                @Override
                                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                                    // Do nothing?
                                }
                            });
                }
            });
        }


    }


}
