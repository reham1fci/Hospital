package com.tree.hospital;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class Profile extends ActionBarActivity {
 ImageView profile_pic;
 TextView full_name_Tv,gender_Tv,country_Tv, city_Tv , special_Tv,phone_Tv,speialize;
    String full_name,gender,country, city , special,phone,image_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        full_name_Tv=(TextView)findViewById(R.id.full_name);
        gender_Tv=(TextView)findViewById(R.id.gender);
        country_Tv=(TextView)findViewById(R.id.country);
        city_Tv=(TextView)findViewById(R.id.city);
        special_Tv=(TextView)findViewById(R.id.special);
        speialize=(TextView)findViewById(R.id.special_tv);

        phone_Tv=(TextView)findViewById(R.id.phone);
        profile_pic=(ImageView)findViewById(R.id.image_pic);
        SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED, 0);
        String id = pref.getString("user_id","");
        String type = pref.getString("kind","");
        webServices web=new webServices();
        if(type.equals("Doctor")){
            Log.d("if", "test");

            web.getDoctor(Profile.this,id,new request_interface() {
                @Override
                public void onResponse(String response) {
                    Log.d("get_doctor",response);
                    try {
                        JSONObject jsonResponse=new JSONObject(response);
                        String first_name=jsonResponse.getString("first_name");
                        String last_name=jsonResponse.getString("last_name");
                         full_name=first_name +" "+last_name;
                         city=jsonResponse.getString("city");
                         country=jsonResponse.getString("country");
                         special=jsonResponse.getString("specialization");
                         phone=jsonResponse.getString("phone");
                        image_str=jsonResponse.getString("picture");
                        gender=jsonResponse.getString("gender");
                        special_Tv.setVisibility(View.VISIBLE);
                        speialize.setVisibility(View.VISIBLE);

                        phone_Tv.setText(phone);
                        country_Tv.setText(country);
                        city_Tv.setText(city);
                        full_name_Tv.setText(full_name);
                        special_Tv.setText(special);
                        gender_Tv.setText(gender);
                       // getimage(image_str);
                        Picasso.with(getApplicationContext())
                                .load(image_str)
                                .into(profile_pic);

                        Log.d("image get from server",image_str);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {
                    Log.d("get_doctor","error");
                }
            });
        }
        else{
            web.getPatient(Profile.this, id, new request_interface() {
                @Override
                public void onResponse(String response) {
                    Log.d("get_PATIENT",response);
                    try {
                        JSONObject jsonResponse=new JSONObject(response);
                        String first_name=jsonResponse.getString("first_name");
                        String last_name=jsonResponse.getString("last_name");
                         full_name=first_name +" "+last_name;
                         city=jsonResponse.getString("city");
                        country=jsonResponse.getString("country");
                         phone=jsonResponse.getString("phone");
                        image_str=jsonResponse.getString("picture");
                        gender=jsonResponse.getString("gender");
                        gender_Tv.setText(gender);
                        phone_Tv.setText(phone);
                        country_Tv.setText(country);
                        city_Tv.setText(city);
                        full_name_Tv.setText(full_name);

                       // getimage(image_str);
                        Picasso.with(getApplicationContext())
                                .load(image_str)
                                .into(profile_pic);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });

        }




    }
    public void getimage(String image2){
       byte[] theByteArray = Base64.decode(image2, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(theByteArray, 0, theByteArray.length);
        profile_pic.setImageBitmap(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
