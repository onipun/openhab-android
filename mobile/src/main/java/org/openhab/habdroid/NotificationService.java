package org.openhab.habdroid;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.impl.client.DefaultHttpClient;
import org.openhab.habdroid.beta.Schedule;
import org.openhab.habdroid.util.AsyncHttpClient;
import org.openhab.habdroid.util.HttpClient;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class NotificationService extends Service {

    private LoginDbInitiate dbA;
    private NotificationManager notificationManager;
    private PendingIntent contentIntent;
    private String name, status;
    private int rowId, swId;
    private Notification myNotication;
    private static final String TAG = NotificationService.class.getSimpleName();


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        String message = intent.getStringExtra("msg");
        Bundle b = intent.getExtras();
        rowId = b.getInt("keyid");
        dbA = dbA.getsLoginDbInitiate(getApplicationContext());

        Cursor c = dbA.getAllReminderData( rowId);
        if (c.moveToFirst()) {
            do {
                name = c.getString(1);
                status = c.getString(5);
            } while (c.moveToNext());
        }

        if (name.equals("LED 1")){
            swId = 1;
        }else if (name.equals("LED 2")){
            swId = 2;
        }else {
            swId = 3;
        }

        if (status.equals("ON")){
            long id = dbA.updateSwitchData(swId, status);

            Bundle notificationBundle = new Bundle();
            String longString = Long.toString(id);
            int keyid = Integer.parseInt(longString);
            notificationBundle.putInt("keyid", keyid);

            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(this,Schedule.class);
            notificationIntent.putExtras(notificationBundle);
            contentIntent=PendingIntent.getActivity(this, 0, notificationIntent, 0);

            ItemsMiddleware.getInstance(this.getApplicationContext()).setItemState("ON", "LED");


        }
        else{
            long id = dbA.updateSwitchData(swId, status);

            Bundle notificationBundle = new Bundle();
            String longString = Long.toString(id);
            int keyid = Integer.parseInt(longString);
            notificationBundle.putInt("keyid", keyid);

            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Intent notificationIntent = new Intent(this,Schedule.class);
            notificationIntent.putExtras(notificationBundle);
            contentIntent=PendingIntent.getActivity(this, 0, notificationIntent, 0);

            ItemsMiddleware.getInstance(this.getApplicationContext()).setItemState("OFF", "LED");
        }

        int icon = R.mipmap.icon;
        CharSequence tickerText = "You got a new notification.";
        long when = System.currentTimeMillis();
        CharSequence contentTitle="Switch Control Management";
        CharSequence contentText = message;

		/*Notification notification=new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify( 123, notification);*/

        //New Notification
        Notification.Builder builder = new Notification.Builder(NotificationService.this);

        builder.setAutoCancel(true);
        builder.setTicker(tickerText);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setSmallIcon(icon);
        builder.setContentIntent(contentIntent);
        //builder.setOngoing(true);
        //builder.setSubText("This is subtext...");   //API level 16
        //builder.setNumber(100);
        builder.build();

        myNotication = builder.getNotification();
        notificationManager.notify(11, myNotication);


        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        try {
            Uri myUri =RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);; // initialize Uri here
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stopSelf();
    }


}
