package com.ewheelers.ewheelersbuyer.Notifications;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.ewheelers.ewheelersbuyer.NavAppBarActivity;
import com.ewheelers.ewheelersbuyer.R;
import com.ewheelers.ewheelersbuyer.SessionStorage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.app.Notification.FLAG_AUTO_CANCEL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "channel-01";
    public static final int ID_BIG_NOTIFICATION = 234;
    public static final int ID_SMALL_NOTIFICATION = 235;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "remote data: " + remoteMessage.getData().toString());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }else{
            Log.e(TAG, "No remote messa");
        }
    }
    //firebase cloud messaging
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());

       /* SharedPreferences sharedPreferences = getSharedPreferences("LOGPREF", MODE_PRIVATE);
        String user_id=sharedPreferences.getString("ID",null);*/
        String tokensytem = new SessionStorage().getStrings(this,SessionStorage.tokenvalue);

        Log.e(TAG, "Notification JSON " + tokensytem);
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");
            Log.e("Push_data", data.toString());
            //parsing json data
            String title = data.getString("title");
            String message = data.getString("message");
            String imageUrl = data.getString("image");
            String uid = data.getString("uid");
            if(uid.equals(tokensytem) && tokensytem != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel serviceChannel = new NotificationChannel(
                            "channel-01",
                            "Foreground Service Channel1",
                            NotificationManager.IMPORTANCE_DEFAULT);

                    if (imageUrl.equals("null")) {
                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(serviceChannel);
                        // mBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;
                        int random = (int) System.currentTimeMillis();
                        Intent notificationIntent = new Intent(this, NavAppBarActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                random, notificationIntent, 0);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notification notification = new NotificationCompat.Builder(this, "channel-01")
                                .setContentTitle(title)
                                .setContentText(message)
                                .setAutoCancel(true)
                                .setSound(alarmSound)
                                .setSmallIcon(R.mipmap.buyer_logo)

                               //.setColor(Color.parseColor("#ed8308"))
                                .setContentIntent(pendingIntent)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .build();
                        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE | FLAG_AUTO_CANCEL;
                        manager.notify(ID_SMALL_NOTIFICATION, notification);

                    } else {


                        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                        bigPictureStyle.setBigContentTitle(title);
                        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
                        bigPictureStyle.bigPicture(getBitmapFromURL(imageUrl));

                        NotificationManager manager = getSystemService(NotificationManager.class);
                        manager.createNotificationChannel(serviceChannel);
                        int random = (int) System.currentTimeMillis();
                        Intent notificationIntent = new Intent(this, NavAppBarActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                                random, notificationIntent, 0);
                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notification notification = new NotificationCompat.Builder(this, "channel-01")
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(bigPictureStyle)
                                .setAutoCancel(true)
                                .setSound(alarmSound)
                                .setSmallIcon(R.mipmap.buyer_logo )
                                .setContentIntent(pendingIntent)
                                //.setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .build();
                        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE | FLAG_AUTO_CANCEL;
                        manager.notify(ID_BIG_NOTIFICATION, notification);

                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }

    }



    private Bitmap getBitmapFromURL(String strURL) {
        Bitmap myBitmap=null;
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return myBitmap;
    }

}