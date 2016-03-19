package com.tree.hospital;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;


public class Prescription extends ActionBarActivity {
    EditText drugs;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        drugs=(EditText)findViewById(R.id.drugs_id);
        send=(Button)findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED,0);
                String sender_name = pref.getString("user_name","");
                SharedPreferences chat_pref=getApplicationContext().getSharedPreferences("chat_pref", 0);
                 String mConvoId = chat_pref.getString("chat_id","");

                String message=drugs.getText().toString();
                message msg = new message();
                msg.setmDate(new Date());
                msg.setmText("Doctor Send Prescription");
                msg.setmSender(sender_name);
                msg.setmPrescription(message);
                messageDataSource.saveMessage(msg,mConvoId );

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prescription, menu);
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
