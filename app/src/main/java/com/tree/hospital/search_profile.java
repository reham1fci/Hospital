package com.tree.hospital;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class search_profile extends ActionBarActivity {
TextView nameTv, phoneTv,specializeTv,cityTv ;
    Button chat, upload_cv;
    String doctor_id,patient_id =null;
    SharedPreferences.Editor editor;
    ImageView profile_pic;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_profile);
        nameTv=(TextView)findViewById(R.id.name);
        phoneTv=(TextView)findViewById(R.id.phone);
        cityTv=(TextView)findViewById(R.id.city);
        specializeTv=(TextView)findViewById(R.id.specialize);
        profile_pic=(ImageView)findViewById(R.id.image_search);
        LinearLayout l=(LinearLayout)findViewById(R.id.LL);
        Log.d("IN SEARCH PREOFILE","IN SEARCH PREOFILE" );
        chat=(Button)findViewById(R.id.chatBtn);
        upload_cv=(Button)findViewById(R.id.upload);
        bundle= getIntent().getExtras();
        upload_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          String cv= bundle.getString("cv");
                Log.d("CV",cv);

           Download download=new Download(search_profile.this);
                download.execute(cv);

            }
        });

        SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED, 0);
        SharedPreferences chat_pref=getApplicationContext().getSharedPreferences("chat_pref", 0);
       editor=chat_pref.edit();

        String category=bundle.getString("category");
        if(category.equals("Doctor")){
            l.setVisibility(View.VISIBLE);
            String specialize=bundle.getString("spec");
            specializeTv.setText(specialize);
            patient_id = pref.getString("user_id","");
            doctor_id=bundle.getString("id");
            upload_cv.setVisibility(View.VISIBLE);
           // editor.putString("sender",doctor_id);
            editor.putString("receiver",doctor_id);
            editor.commit();



        }
        else{
            doctor_id = pref.getString("user_id","");
             patient_id=bundle.getString("id");
           // editor.putString("sender",patient_id);
            editor.putString("receiver",patient_id);
            editor.commit();

        }

       String Full_name= bundle.getString("name");
       String city=  bundle.getString("city");
       String phone= bundle.getString("phone");
        nameTv.setText(Full_name);
        cityTv.setText(city);
        phoneTv.setText(phone);
         String image_str=bundle.getString("picture");
        editor.putString("receiver_name",Full_name);
        editor.commit();
       // getimage(image_str);
        Picasso.with(getApplicationContext())
                .load(image_str)
                .into(profile_pic);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webServices web= new webServices();
                web.chat(search_profile.this,doctor_id,patient_id,new request_interface() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse= null;
                        try {
                            jsonResponse = new JSONObject(response);
                            String chat_id=jsonResponse.getString("chat_id");

                                Log.d("chat_id", chat_id);
                            Intent intent = new Intent(getApplicationContext(), Chat.class);
                            intent.putExtra("chat_id",chat_id);
                            editor.putString("chat_id",chat_id);
                            editor.commit();

                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

            }
        });


    }

    public void getimage(String image2){
      /*  byte[] theByteArray = Base64.decode(image2, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(theByteArray, 0, theByteArray.length);*/
        Bitmap bitmap = BitmapFactory.decodeFile(image2);
        profile_pic.setImageBitmap(bitmap);
    }
    public void getDoctorCv(String cv) throws IOException {

        byte[] theByteArray = Base64.decode(cv, 0);

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath(),"doctor_cv.pdf");
            FileOutputStream fOut = new FileOutputStream(myFile);
            fOut.write(theByteArray);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_profile, menu);
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
