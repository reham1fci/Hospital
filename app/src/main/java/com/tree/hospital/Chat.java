package com.tree.hospital;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Date;


public class Chat extends ActionBarActivity implements View.OnClickListener, messageDataSource.MessagesCallbacks {
    TextView fireData;
    Firebase mRef;
    public static final String USER_EXTRA = "USER";

    public static final String TAG = "ChatActivity";

    private ArrayList<message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private String mConvoId;
    private messageDataSource.MessagesListener mListener;
    EditText sender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SharedPreferences chat_pref=getApplicationContext().getSharedPreferences("chat_pref", 0);
        mRecipient = chat_pref.getString("receiver_name","");
        mListView = (ListView)findViewById(R.id.messages_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages,getApplicationContext());
        mListView.setAdapter(mAdapter);

        setTitle(mRecipient);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button sendMessage = (Button)findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);
        Bundle bundle=getIntent().getExtras();
       // String[] ids = {"Ajay","-", "aya"};
      //  Arrays.sort(ids);
        mConvoId = chat_pref.getString("chat_id","");
        mListener = messageDataSource.addMessagesListener(mConvoId,this);
    }


    @Override
    public void onClick(View v) {
        EditText newMessageView = (EditText)findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
       SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED,0);
        String sender_name = pref.getString("user_name","");

        newMessageView.setText("");
        message msg = new message();
        msg.setmDate(new Date());
        msg.setmText(newMessage);
        msg.setmSender(sender_name);
        msg.setmPrescription("null");
        messageDataSource.saveMessage(msg, mConvoId);
    }
    @Override
    public void onMessageAdded(message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        mListView.smoothScrollByOffset(mMessages.size());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageDataSource.stop(mListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences pref =getSharedPreferences(webServices.SHARED, 0);
        String sender_name = pref.getString("kind","");
        if(sender_name.equals("Doctor")){
       getMenuInflater().inflate(R.menu.menu_chat,menu);
            return true;
        }
        else{
            return false;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.r){
            Intent intent=new Intent(getApplicationContext(),Prescription.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
