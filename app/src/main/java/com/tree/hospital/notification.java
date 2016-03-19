package com.tree.hospital;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class notification extends Service implements messageDataSource.MessagesCallbacks {

NotificationCompat.Builder mBuilder;
        public static int count;
        String tag;
    int chat_count_notification;
    String mConvoId;
    private messageDataSource.MessagesListener mListener;
    SharedPreferences chat_pref;
    SharedPreferences chat_list;
    SharedPreferences.Editor editor;
public notification() {

        }

@Override
public void onCreate() {
    super.onCreate();
    count = 0;
    mBuilder = new NotificationCompat.Builder(getApplicationContext());
    chat_pref =getSharedPreferences("chat_pref", 0);
    chat_list = getSharedPreferences("chat_list", 0);
    editor = chat_list.edit();
   /* editor = chat_pref.edit();
    chat_list = getSharedPreferences("chat_list", Context.MODE_MULTI_PROCESS);
    int chat_list_length = chat_list.getInt("length", 0);
    Log.d("chat_list_length", String.valueOf(chat_list_length));
    for (int i = 0; i < chat_list_length; i++) {
        final String chat_id = chat_list.getString("chat_id"+i,"");
        Log.d("chat_id"+i, chat_id);



        mListener = messageDataSource.addMessagesListener(chat_id, new messageDataSource.MessagesCallbacks() {
            @Override
            public void onMessageAdded(message message) {
        notification_msg(message,chat_id);
              //  Log.d("chat_id", chat_id);

            }
        });*/


}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mConvoId = chat_pref.getString("chat_id", "");

        mListener = messageDataSource.addMessagesListener(mConvoId,this);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
        }




    public void notification_msg(message message, String chat_id){
        SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED, 0);
        String sender_name = pref.getString("user_name","");
        int  count_chat = chat_pref.getInt("count",0);
     //   Log.d("chat_count", String.valueOf(count_chat));

        if (!message.getmSender().equals(sender_name)) {
            tag=tag.valueOf(count);
            if(count>count_chat){
                count=count+1;
                editor.putInt(mConvoId,count);
                editor.commit();
                mBuilder.setSmallIcon(R.drawable.ic_launcher).setContentTitle(tag).setTicker(tag).setContentText(message.getmSender() + ":-" + message.getmText()).setDefaults(Notification.DEFAULT_SOUND);
                Intent resultIntent = new Intent(this, Chat.class);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, count, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(count,mBuilder.build());
            }

        }
    }

    @Override
    public void onMessageAdded(message message) {
        SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED, 0);
        String sender_name = pref.getString("user_name","");
        chat_count_notification = chat_list.getInt(mConvoId, 0);
          Log.d("chat_count", String.valueOf(chat_count_notification));
        Log.d("count", String.valueOf(count));

        Intent resultIntent;
        count=count+1;

        if (!message.getmSender().equals(sender_name)) {
            tag=tag.valueOf(count);
            if(count>chat_count_notification){

                mBuilder.setSmallIcon(R.drawable.ic_launcher).setContentTitle("Hospital")
                        .setTicker("Hospital").setContentText(message.getmSender() + ":-"
                        + message.getmText()).setDefaults(Notification.DEFAULT_SOUND);
                if(!message.getmPrescription().equals("null")){
                    resultIntent = new Intent(this, Display_Prescription.class);
                    resultIntent.putExtra("rosheta",message.getmPrescription());
                }
                else {
                    resultIntent = new Intent(this, Chat.class);
                }                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, count, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(count,mBuilder.build());
                editor.putInt(mConvoId,count);
                editor.commit();

        }
    }}
}