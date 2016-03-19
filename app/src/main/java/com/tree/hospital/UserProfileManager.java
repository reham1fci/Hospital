package com.tree.hospital;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.batch.android.Batch;
import com.batch.android.BatchUserProfile;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thy on 12/03/2016.
 */
public class UserProfileManager {
    private static final String TAG = "UserProfileManager";

    private static final String PREF_LOGIN_TYPE = "loginType";

    private static UserProfileManager instance = new UserProfileManager();

    /**
     * Root Firebase
     */
    private Firebase firebase = new Firebase("https://chatgg.firebaseio.com/");

    private Firebase userFirebase;

    private UserProfile currentUser;

    public static UserProfileManager getInstance() {
        return instance;
    }

    // ---------- Login information methods -------

    public void readAuthInfo() {
        AuthData auth = firebase.getAuth();
        if (auth != null && "anonymous".equalsIgnoreCase(auth.getProvider())) {
            currentUser = new UserProfile();
            currentUser.UID = auth.getUid();
            currentUser.name = "Anonymous";
            currentUser.email = "anon@anon.com";
            currentUser.profileImage = "";

            userFirebase = firebase.child("users").child(currentUser.UID);
        }
    }

    public boolean isLoggedIn() {
        return currentUser != null || getCurrentLoginType() == LoginType.ANONYMOUS;
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public LoginType getCurrentLoginType() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(appfirebase.getInstance());
        try {
            return LoginType.valueOf(prefs.getString(PREF_LOGIN_TYPE, LoginType.LOGGED_OUT.toString()));
        } catch (IllegalArgumentException e) {
            return LoginType.LOGGED_OUT;
        }
    }

    public void setCurrentLoginType(LoginType loginType) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(appfirebase.getInstance()).edit();
        editor.putString(PREF_LOGIN_TYPE, loginType.toString());
        editor.apply();
    }

    // ---------- Login methods -----------------

    public void loginAnonymously() {
        firebase.authAnonymously(new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.i(TAG, "Signed in into Firebase");
                currentUser = new UserProfile();
                currentUser.UID = authData.getUid();
                Log.d( "auth_id", currentUser.UID);
                currentUser.name = "Anonymous";
                currentUser.email = "anon@anon.com";
                currentUser.profileImage = "";

                setCurrentLoginType(LoginType.ANONYMOUS);

                userFirebase = firebase.child("users").child(currentUser.UID);

                final Map<String, String> userData = new HashMap<>();
                userData.put("provider", "anonymous");
                userData.put("name", currentUser.name);
                userData.put("email", currentUser.email);
                userData.put("profileImageURL", currentUser.profileImage);
                userFirebase.setValue(userData);
                BatchUserProfile batchUser = Batch.getUserProfile();
                if (batchUser != null) {
                    batchUser.setCustomID(currentUser.UID);
                } else {
                    Log.e(TAG, "Error while getting BatchUserProfile, custom ID will not work.");
                }

                //    appfirebase.getEventBus().post(new UserLoggedInEvent());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e(TAG, "Error while signing in Firebase " + firebaseError.getMessage());

                //  appfirebase.getEventBus().post(new UserLoginFailedEvent());
            }
        });
    }


    public void logout() {
        firebase.unauth();
        setCurrentLoginType(LoginType.LOGGED_OUT);
        currentUser = null;
        userFirebase = null;
    }

    // ---------- Inner classes/enums -----------

    public static class UserProfile {
        public String UID;
        public String name;
        public String email;
        public String profileImage;
    }

    public enum LoginType {
        GOOGLE,
        ANONYMOUS,
        LOGGED_OUT
    }

}