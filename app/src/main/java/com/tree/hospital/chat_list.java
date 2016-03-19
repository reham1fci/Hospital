package com.tree.hospital;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class chat_list extends ActionBarActivity {
    String[] list_array=null;
    ListView list;
    String[] ids=null;
    String[] chat_ids=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        list=(ListView)findViewById(R.id.chat_list);
        SharedPreferences pref =getApplicationContext().getSharedPreferences(webServices.SHARED, 0);
        final SharedPreferences chat_list =getSharedPreferences("chat_list",Context.MODE_MULTI_PROCESS);
        final SharedPreferences.Editor editor_list=chat_list.edit();
        String user_id= pref.getString("user_id","");
        String profile_kind=pref.getString("kind","");
        webServices web= new webServices();
        final ArrayAdapter<String> list_adapter=new ArrayAdapter<String>(this,R.layout.spinner_adapter,R.id.spinner_tv);
        list.setAdapter(list_adapter);
        if(profile_kind.equals("Doctor")){
            web.doctor_chat(this, user_id, new request_interface() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse=new JSONObject(response);
                        JSONArray jsonArray= jsonResponse.getJSONArray("list");
                        list_array=new String[jsonArray.length()];
                        ids=new String[jsonArray.length()];
                        chat_ids=new String[jsonArray.length()];

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject search_object=jsonArray.getJSONObject(i) ;
                            String firstName =search_object.getString("first_name");
                            String lastName =search_object.getString("last_name");
                            String patient_id =search_object.getString("patient_id");
                            String chat_id =search_object.getString("chat_id");
                            list_array[i]=firstName+" "+lastName;
                            ids[i]=patient_id;
                            chat_ids[i]=chat_id;
                            if(!chat_list.contains(chat_id)){
                            editor_list.putInt(chat_id,0);
                            editor_list.commit();}

                        }
                        editor_list.putInt("length",jsonArray.length());
                        editor_list.commit();
                        list_adapter.addAll(list_array);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    }

                    @Override
                    public void onError () {

                    }
                }

                );
            }
            else{
            web.patient_chat(this, user_id, new request_interface() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse=new JSONObject(response);
                        JSONArray jsonArray= jsonResponse.getJSONArray("list");
                        list_array=new String[jsonArray.length()];
                        ids=new String[jsonArray.length()];
                        chat_ids=new String[jsonArray.length()];

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject search_object=jsonArray.getJSONObject(i) ;
                            String firstName =search_object.getString("first_name");
                            String lastName =search_object.getString("last_name");
                            String doctor_id =search_object.getString("doctor_id");
                            String chat_id =search_object.getString("chat_id");
                            list_array[i]=firstName+" "+lastName;

                            ids[i]=doctor_id;
                            chat_ids[i]=chat_id;
                            if(!chat_list.contains(chat_id)){
                                editor_list.putInt(chat_id,0);
                                editor_list.commit();}


                        }
                        editor_list.putInt("length",jsonArray.length());
                        editor_list.commit();


                        list_adapter.addAll(list_array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });

            }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences chat_pref=getApplicationContext().getSharedPreferences("chat_pref", 0);
                SharedPreferences.Editor editor=chat_pref.edit();
                String chat_id=chat_ids[position];
                String receiver=ids[position];
                String receiver_name=list_array[position];
                editor.putString("receiver",receiver);
                editor.putString("receiver_name",receiver_name);
                editor.putString("chat_id",chat_id);
                editor.commit();
                Intent intent=new Intent(getApplicationContext(),Chat.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
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
