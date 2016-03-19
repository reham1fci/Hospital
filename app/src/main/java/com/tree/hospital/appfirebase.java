package com.tree.hospital;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.firebase.client.Firebase;

/**
 * Created by thy on 29/02/2016.
 */
public class appfirebase extends Application {
    private static final String  API_KEY= "DEV56E3365FBBCCAB3308DB4E1C8AA";
    private static final String  SENDER_ID= "620921077393";



    private static appfirebase instance;

    public static appfirebase getInstance() {
        return instance;
    }

   /* public static Bus getEventBus() {
        return instance.eventBus;
    }

    private Bus eventBus;*/
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Batch.setConfig(new Config(API_KEY));
        Batch.Push.setGCMSenderId(SENDER_ID);

        instance = this;

    }
}
