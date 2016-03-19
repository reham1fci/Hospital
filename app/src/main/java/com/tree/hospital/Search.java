package com.tree.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Search extends ActionBarActivity {
 EditText search_word;
    Spinner searchBy_spinner;
    ImageButton search;
    String[] spinner_array=null;
    String[] list_array=null;
    String [] ids=null;

    String searchBy, search_for=null;
ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle bundle=getIntent().getExtras();
        search_for=bundle.getString("search_for");
        search_word=(EditText)findViewById(R.id.search_word);
        searchBy_spinner =(Spinner)findViewById(R.id.search_type);
        list=(ListView)findViewById(R.id.list);
        search=(ImageButton)findViewById(R.id.search);
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(this,R.layout.spinner_adapter,R.id.spinner_tv);
        final ArrayAdapter<String> list_adapter=new ArrayAdapter<String>(this,R.layout.spinner_adapter,R.id.spinner_tv);
          list.setAdapter(list_adapter);
        searchBy_spinner.setAdapter(spinner_adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final String Id=ids[position];
                webServices web=new webServices();
                if(search_for.equals("Doctor")){
                    Log.d("if","test" );

                    web.getDoctor(Search.this,Id,new request_interface() {
                   @Override
                   public void onResponse(String response) {
                       Log.d("get_doctor",response);
                       try {
                           JSONObject jsonResponse=new JSONObject(response);
                           String first_name=jsonResponse.getString("first_name");
                           String last_name=jsonResponse.getString("last_name");
                           String full_name=first_name +" "+last_name;
                           String city=jsonResponse.getString("city");
                           String specialization=jsonResponse.getString("specialization");
                           String phone=jsonResponse.getString("phone");
                           String picture=jsonResponse.getString("picture");
                           String cv=jsonResponse.getString("cv");
                           Intent intent=new Intent(getApplicationContext(),search_profile.class);
                           intent.putExtra("name",full_name);
                           intent.putExtra("city",city);
                           intent.putExtra("spec",specialization);
                           intent.putExtra("phone",phone);
                           intent.putExtra("picture",picture);
                           intent.putExtra("category","Doctor");
                           intent.putExtra("cv",cv);
                           Log.d("CV BEFORE INTENT ",cv);
                           intent.putExtra("id",Id);
                           startActivity(intent);
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
                    web.getPatient(Search.this, Id, new request_interface() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("get_PATIENT",response);
                            try {
                                JSONObject jsonResponse=new JSONObject(response);
                                String first_name=jsonResponse.getString("first_name");
                                String last_name=jsonResponse.getString("last_name");
                                String full_name=first_name +" "+last_name;
                                String city=jsonResponse.getString("city");
                                String phone=jsonResponse.getString("phone");
                                String picture=jsonResponse.getString("picture");

                                Intent intent=new Intent(getApplicationContext(),search_profile.class);
                                intent.putExtra("name",full_name);
                                intent.putExtra("city",city);
                                intent.putExtra("phone",phone);
                                intent.putExtra("category","patient");
                                intent.putExtra("picture",picture);
                                intent.putExtra("id",Id);
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

            }
        });


        if(search_for.equals("Doctor")){
            spinner_array= new String[]{"name", "country", "city", "specialization"};
            spinner_adapter.addAll(spinner_array);
        }
        else{
            spinner_array= new String[]{"name", "country", "city"};

            spinner_adapter.addAll(spinner_array);

        }
        searchBy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchBy =  spinner_array[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             search.setBackgroundResource(R.drawable.search_graypng);
                 webServices web=new webServices();
                String searchWord= search_word.getText().toString();
                if(search_for.equals("Doctor")) {
                    web.search_doctor(Search.this, searchBy, searchWord, new request_interface() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("message",response);
                            try {
                               JSONObject jsonResponse=new JSONObject(response);
                               JSONArray jsonArray= jsonResponse.getJSONArray("search");
                                list_array=new String[jsonArray.length()];
                                ids=new String[jsonArray.length()];

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject search_object=jsonArray.getJSONObject(i) ;
                                    String fName =search_object.getString("first_name");
                                    String lName =search_object.getString("last_name");
                                    String id =search_object.getString("doctor_id");
                                    list_array[i]="Doctor: "+fName+" "+lName;
                                    ids[i]=id;


                                }
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
                else{
                    web.search_patient(Search.this, searchBy, searchWord,new request_interface() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("message",response);
                            try {
                                JSONObject jsonResponse=new JSONObject(response);
                                JSONArray jsonArray= jsonResponse.getJSONArray("search");
                                list_array=new String[jsonArray.length()];
                                ids=new String[jsonArray.length()];

                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject search_object=jsonArray.getJSONObject(i) ;
                                    String fName =search_object.getString("first_name");
                                    String lName =search_object.getString("last_name");
                                    String id =search_object.getString("patient_id");
                                    list_array[i]="patient: "+fName+" "+lName;
                                    ids[i]=id;

                                }
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

            }



    });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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
