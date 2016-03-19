package com.tree.hospital;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by thy on 27/02/2016.
 */
public class webServices {


    SharedPreferences.Editor editor;
    public static String SHARED="user_shared_preference";
    public static String url_upload="http://super-buzz-tree.hostoi.com/uploads/";
    public static String DOCTOR_FNAME="doctor_fname";
    public static String DOCTOR_LNAME="doctor_lname";
    public static String DOCTOR_USERNAME="doctor_userName";
    public static String DOCTOR_PASSWORD="doctor_password";
    public static String DOCTOR_GENDER="doctor_gender";
    public static String DOCTOR_EMAIL="doctor_email";
    public static String DOCTOR_COUNTRY="doctor_country";
    public static String DOCTOR_CITY="doctor_city";
    public static String DOCTOR_PHONE="doctor_phone";
    public static String DOCTOR_CV="doctor_cv";
    public static String DOCTOR_PHOTO="doctor_photo";
    public static String DOCTOR_ID="doctor_id";
    public static String DOCTOR_SPECIALIZATION="doctor_specialization";
//----------------------------------------------------

    public static String PATIENT_FNAME="patient_fname";
    public static String PATIENT_LNAME="patient_lname";
    public static String PATIENT_USERNAME="patient_userName";
    public static String PATIENT_PASSWORD="patient_password";
    public static String PATIENT_GENDER="patient_gender";
    public static String PATIENT_EMAIL="patient_email";
    public static String PATIENT_COUNTRY="patient_country";
    public static String PATIENT_CITY="patient_city";
    public static String PATIENT_PHONE="patient_phone";
    public static String PATIENT_CV="doctor_cv";
    public static String PATIENT_ID="patient_id";

    public static String PATIENT_PHOTO="patient_photo";
    //-------------------------------------------------------

    public static String SEARCH_BY="search_by";
    public static String SEARCH_WORD="search_word";



    //--------------------------------------
public static String TAG="tag";
    public static String ADD_PATIENT_TAG="add_patient";
    public static String ADD_DOCTOR_TAG="add_doctor";
    public static String DOCTOR_LOGIN_TAG="doctor_login";
    public static String PATIENT_LOGIN_TAG="patient_login";
    public static String SEARCH_DOCTOR_TAG="search_doctor";
    public static String SEARCH_PATIENT_TAG="search_patient";
    public static String GET_PATIENT_TAG="getPatient";
    public static String GET_DOCTOR_TAG="getDoctor";
    public static String CHAT_TAG="chat";

    public static String DOCTOR_CHAT_TAG="doctor_chatList";
    public static String PATIENT_CHAT_TAG="patient_chatList";

    //------------------------------------
    RequestQueue queue;
    String url = "http://super-buzz-tree.hostoi.com/hospital_tags.php";
//----------------------------------
    public void doctorLogin(final Activity activity, final String doctor_userName, final String doctor_password){

        queue = Volley.newRequestQueue(activity);
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String login_response=jsonObject.getString("login_response");
                    messageDataSource M=new messageDataSource();
                    if(login_response.equals("done")) {
                        //UserProfileManager.getInstance().loginAnonymously();
                      //  M.listner_change();
                        Intent serviceIntent = new Intent(activity, notification.class);
                        activity.startService(serviceIntent);
                        String id = jsonObject.getString("doctor_id");
                        editor = activity.getSharedPreferences(SHARED,0).edit();
                        SharedPreferences chat_pref=activity.getSharedPreferences("chat_pref", 0);
                        SharedPreferences.Editor editor_chat=chat_pref.edit();
                        editor.putString("user_id",id);
                        editor.putString("user_name",doctor_userName);
                        editor_chat.putString("sender",id);
                        editor.commit();
                        editor_chat.commit();
                        Intent intent= new Intent(activity,Home.class);
                        activity.startActivity(intent);

                    }
                    else if(login_response.equals("incorrect password")){
                        Toast.makeText(activity,"incorrect password please try again",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(activity,"please signup you don't have account ",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {

                }
            }


        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity,"connection error ",Toast.LENGTH_LONG).show();


            }
        }){
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(DOCTOR_USERNAME, doctor_userName);
                params.put(DOCTOR_PASSWORD, doctor_password);
                params.put(TAG,DOCTOR_LOGIN_TAG );
                return params;
            }

        };
        queue.add(request);

    }
    public void patientLogin(final Activity activity, final String patient_userName, final String patient_password){
        queue = Volley.newRequestQueue(activity);
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                JSONObject jsonObject=new JSONObject(response);
                String login_response=jsonObject.getString("login_response");
                if(login_response.equals("done")) {
                    Intent serviceIntent = new Intent(activity, notification.class);
                    activity.startService(serviceIntent);
                    String id = jsonObject.getString("patient_id");
                    editor = activity.getSharedPreferences(SHARED,0).edit();
                    editor.putString("user_id",id);
                    editor.putString("user_name",patient_userName);
                    editor.commit();
                    SharedPreferences chat_pref=activity.getSharedPreferences("chat_pref", 0);
                    SharedPreferences.Editor editor_chat=chat_pref.edit();
                    editor_chat.putString("sender",id);
                    editor_chat.commit();
                    Intent intent= new Intent(activity,Home.class);
                    activity.startActivity(intent);

                }
                else if(login_response.equals("incorrect password")){
                    Toast.makeText(activity,"incorrect password please try again",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(activity,"please signup you don't have account ",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                    Toast.makeText(activity,"connection error ",Toast.LENGTH_LONG).show();

            }



        }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(PATIENT_USERNAME,patient_userName );
                params.put(PATIENT_PASSWORD, patient_password);
                params.put(TAG,PATIENT_LOGIN_TAG );
                return params;
            }};
        queue.add(request);

    }
    public void addDoctor(final Activity activity, final String firstName, final String lastName, final String userName,
                          final String Password, final String Email, final String Country, final String City,
                          final String Phone, final String Specialization, final String gender, final String photo, final String cv){
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(activity," register done",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(activity,Login.class);
                activity.startActivity(intent);
            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("responseerror",error.toString());

            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(DOCTOR_FNAME, firstName);
                params.put(DOCTOR_LNAME, lastName);
                params.put(DOCTOR_USERNAME, userName);
                params.put(DOCTOR_CITY, City);
                params.put(DOCTOR_COUNTRY, Country);
                params.put(DOCTOR_PASSWORD, Password);
                params.put(DOCTOR_CV,cv );
                params.put(DOCTOR_PHOTO,photo );
                params.put(DOCTOR_PHONE, Phone);
                params.put(DOCTOR_EMAIL, Email);
                params.put(DOCTOR_GENDER, gender);
                params.put(DOCTOR_SPECIALIZATION, Specialization);
                params.put(TAG, ADD_DOCTOR_TAG);
                return params;
            }
        };
        queue.add(request);
    }
    public void addPatient(final Activity activity,final String firstName, final String lastName, final String userName,
                           final String Password, final String Email, final String Country, final String City,
                           final String Phone, final String gender, final String photo){
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(activity," register done",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(activity,Login.class);
                activity.startActivity(intent);
            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(PATIENT_FNAME, firstName);
                params.put(PATIENT_LNAME, lastName);
                params.put(PATIENT_USERNAME, userName);
                params.put(PATIENT_CITY, City);
                params.put(PATIENT_COUNTRY, Country);
                params.put(PATIENT_PASSWORD, Password);
                params.put(PATIENT_PHOTO, photo);
                params.put(PATIENT_PHONE, Phone);
                params.put(PATIENT_EMAIL, Email);
                params.put(PATIENT_GENDER, gender);
                params.put(TAG, ADD_PATIENT_TAG);
                return params;
            }
        };
        queue.add(request);

    }
    public void getDoctor(Activity activity, final String id, final request_interface request_interface){
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            request_interface.onResponse(response);
                Log.d("get_doctorweb",response);

            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                request_interface.onError();


            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(DOCTOR_ID, id);
                params.put(TAG,GET_DOCTOR_TAG );
                return params;
            }
            };
        queue.add(request);

    }
    public void getPatient(Activity activity, final String id, final request_interface request_interface ){
        queue = Volley.newRequestQueue(activity );
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            request_interface.onResponse(response);

            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                request_interface.onError();

            }
        }){
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(PATIENT_ID, id);
                params.put(TAG, GET_PATIENT_TAG);
                return params;}
            };
        queue.add(request);

    }
    public void search_doctor(final Activity activity, final String search_by, final String search_word, final request_interface object) {
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
              object.onResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(SEARCH_BY, search_by);
                params.put(SEARCH_WORD, search_word);
                params.put(TAG, SEARCH_DOCTOR_TAG);
                return params;
            }


        };
        queue.add(request);

    }
    public void search_patient(final Activity activity, final String search_by, final String search_word , final request_interface object) {
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              Log.d("RESPONSE",response);
                object.onResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(SEARCH_BY, search_by);
                params.put(SEARCH_WORD, search_word);
                params.put(TAG, SEARCH_PATIENT_TAG);
                return params;
            }


        };
        queue.add(request);

    }
    public void chat(final Activity activity, final String doctor_id, final String patient_id , final request_interface object) {
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSEchat",response);
                object.onResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(DOCTOR_ID, doctor_id);
                params.put(PATIENT_ID, patient_id);
                params.put(TAG,CHAT_TAG);
                return params;
            }


        };
        queue.add(request);

    }
    public void patient_chat(final Activity activity, final String patient_id,  final request_interface object) {
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
                object.onResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(PATIENT_ID, patient_id);
                params.put(TAG, PATIENT_CHAT_TAG);
                return params;
            }


        };
        queue.add(request);

    }
    public void doctor_chat(final Activity activity, final String doctor_id,  final request_interface object) {
        queue = Volley.newRequestQueue(activity);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE",response);
                object.onResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                object.onError();

            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<String, String>();
                params.put(DOCTOR_ID, doctor_id);
                params.put(TAG, DOCTOR_CHAT_TAG);
                return params;
            }


        };
        queue.add(request);

    }


}



