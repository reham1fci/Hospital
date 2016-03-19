package com.tree.hospital;

import android.util.Log;

import com.batch.android.Batch;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by thy on 29/02/2016.
 */
public class messageDataSource {
    private static final Firebase sRef = new Firebase("https://chatgg.firebaseio.com/");
    private static SimpleDateFormat readFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
    private static final String TAG = "MessageDataSource";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_SENDER = "sender";
    private static final String COLUMN_PRESCRIPTION = "prescription";


    public static void saveMessage(message message, String convoId) {
        Date date = message.getmDate();
        String key = readFormat.format(date);
        HashMap<String, String> msg = new HashMap<>();
        msg.put(COLUMN_TEXT, message.getmText());
        msg.put(COLUMN_SENDER, message.getmSender());
        msg.put(COLUMN_PRESCRIPTION, message.getmPrescription());

        sRef.child(convoId).child(key).setValue(msg);
    }

    public static MessagesListener addMessagesListener(String convoId, final MessagesCallbacks callbacks) {
        MessagesListener listener = new MessagesListener(callbacks);
        sRef.child(convoId).addChildEventListener(listener);
        return listener;

    }
public void listner_change(){
    sRef.addAuthStateListener(new Firebase.AuthStateListener() {
        @Override
        public void onAuthStateChanged(AuthData authData) {
            if (authData != null) {
                // user is logged in
                Log.d("test","test firebase");
            } else {
                // user is not logged in
                Log.d("test","test not firebase");

            }
        }
    }) ;

    AuthData firebaseAuthData = sRef.getAuth();

    Batch.User.getEditor()
            .setIdentifier(firebaseAuthData.getUid())
            .save(); // Don't forget to save the changes!
}
    public static void stop(MessagesListener listener) {
        sRef.removeEventListener(listener);
    }

    public static class MessagesListener implements ChildEventListener {
        private MessagesCallbacks callbacks;

        MessagesListener(MessagesCallbacks callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String, String> msg = (HashMap) dataSnapshot.getValue();
            message message = new message();
            message.setmSender(msg.get(COLUMN_SENDER));
//            Log.d("message_list",msg.get(COLUMN_SENDER));
            message.setmText(msg.get(COLUMN_TEXT));
            message.setmPrescription(msg.get(COLUMN_PRESCRIPTION));

            try {
                message.setmDate(readFormat.parse(dataSnapshot.getKey()));
            } catch (Exception e) {
                Log.d(TAG, "Couldn't parse date" + e);
            }
            if (callbacks != null) {
                callbacks.onMessageAdded(message);
            }
            else {
                notification.count = 0;
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }

    }
    public interface MessagesCallbacks{
        public void onMessageAdded(message message);
    }
}
