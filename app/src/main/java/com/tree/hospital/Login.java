package com.tree.hospital;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.batch.android.Batch;


public class Login extends ActionBarActivity {
EditText user_name, password;
Button login_btn, sign_up_btn;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user_name=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        login_btn=(Button)findViewById(R.id.login);
        sign_up_btn=(Button)findViewById(R.id.sign_up);
        final webServices web=new webServices();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(Login.this, login_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String userName=user_name.getText().toString();
                        String Password=password.getText().toString();
                        String click_item=item.getTitle().toString();
                        editor = getApplicationContext().getSharedPreferences(webServices.SHARED,0).edit();
                        editor.putString("kind",click_item);
                        editor.commit();
                        if(click_item.equals("Doctor")){
                            web.doctorLogin(Login.this,userName,Password);

                        }
                        else{

                            web.patientLogin(Login.this,userName,Password);
                        }

                        return true;
                    }
                });
                popup.show();//showing popup menu

    }
        });
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Login.this, sign_up_btn);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String click_item=item.getTitle().toString();
                        if(click_item.equals("Doctor")){
                            Intent intent= new Intent(getApplicationContext(),Doctor_Signup.class);
                            startActivity(intent);
                        }
                        else{
                            Intent intent= new Intent(getApplicationContext(),Patient_Signup.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu

            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        Batch.onStart(this);
    }

    @Override
    protected void onStop()
    {
        Batch.onStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Batch.onDestroy(this);
        //FirebaseApplication.getEventBus().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Batch.onNewIntent(this, intent);
    }
}