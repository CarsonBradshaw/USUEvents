package redteam.usuevents;

import android.app.Notification;
import android.app.Service;

/**
 * Created by Carson on 3/16/2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import redteam.usuevents.models.EventModel;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public EventModel eventModel;

    private static final Map<String, String> topicTranslationMap = createMap();
    private static Map<String, String> createMap()
    {
        Map<String,String> topicTranslationMap = new HashMap<String,String>();
        topicTranslationMap.put("mBasketball", "Men's Basketball");
        topicTranslationMap.put("mCrossCountry", "Men's Cross Country");
        topicTranslationMap.put("mFootball", "Football");
        topicTranslationMap.put("mGolf", "Men's Golf");
        topicTranslationMap.put("mTennis", "Men's Tennis");
        topicTranslationMap.put("mTrack", "Men's Track");
        topicTranslationMap.put("wBasketball", "Women's Basketball");
        topicTranslationMap.put("wCrossCountry", "Women's Cross Country");
        topicTranslationMap.put("wGymnastics", "Women's Gymnastics");
        topicTranslationMap.put("wSoccer", "Women's Soccer");
        topicTranslationMap.put("wSoftball", "Softball");
        topicTranslationMap.put("wTennis", "Women's Tennis");
        topicTranslationMap.put("wTrack", "Women's Track");
        topicTranslationMap.put("wVolleyball", "Women's Volley");
        topicTranslationMap.put("parties", "Parties");
        topicTranslationMap.put("miscUsu", "USU Sponsored");
        topicTranslationMap.put("userSubmitted", "User Submitted");
        return topicTranslationMap;
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {




        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, remoteMessage.toString());



        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Map<String, String> map = new HashMap<>();
        map = remoteMessage.getData();



        try {
            sendNotification(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param map FCM message body received.
     */
    private void sendNotification(Map<String, String> map) throws IOException {
        //handler to dealy notifications
        Handler handler = new Handler(Looper.getMainLooper());

        eventModel = new EventModel();
        eventModel.setDescription(map.get("description"));
        eventModel.setLat(map.get("lat"));
        eventModel.setLng(map.get("lng"));
        eventModel.setNotified((Boolean.getBoolean(map.get("notified"))));
        eventModel.setStartDateTime(map.get("startDateTime"));
        eventModel.setTitle(map.get("title"));
        eventModel.setTopic(map.get("topic"));
        eventModel.setVoteCt(map.get("voteCt"));



        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra("EventModel",eventModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String lat = map.get("lat");
        String lng = map.get("lng");
        URL url = null;
        try {
            url = new URL("http://maps.google.com/maps/api/staticmap?center=" + lat + "," + lng + "&zoom=16.5&size=600x600&sensor=false&markers=color:blue%7C"+lat+","+lng);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());


        String fullDate = map.get("startDateTime");
        String input_date= fullDate.substring(0,10);
        String input_time = fullDate.substring(11,16);
        SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
        Date dt1= null;
        try {
            dt1 = format1.parse(input_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format2=new SimpleDateFormat("EEEE");
        String finalDay=format2.format(dt1);

        String outputTime = "";
//        SimpleDateFormat dateFormatExpression = new SimpleDateFormat("hh:mm a");
//
//        try {
//            Date d  = dateFormatExpression.parse(input_time);
//            outputTime = d.toString();
//            Log.d("Outputtime: " , outputTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        try {

            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(input_time);
            outputTime=_12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mountain)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.usueventslogo))
                .setContentTitle(topicTranslationMap.get(map.get("topic")) + ": " + map.get("title"))
                .setContentText("Event starts" + " " + finalDay + " at " + outputTime)
                .setShowWhen(false)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bmp))
                .setContentIntent(pendingIntent);

        final NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        handler.postDelayed(new Runnable() {
            public void run() {
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
        }, 30000); //second param is the time delay in milliseconds (1000ms/second)


    }
}
