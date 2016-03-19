package com.tree.hospital;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class Doctor_Signup extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
EditText first_name,user_name,last_name,password
        ,confirm_password,email,country,city,phone,specialization;
    Spinner gender;
    Button sign_up, upload_image,upload_cv;
    String genderSelected;
  static   int RESULT_LOAD_IMG=1;
    static   int RESULT_LOAD_CV=2;
    String imgDecodableString;

    EditText file_name;
    String  cv_string , image_str ="";
    final static String[]Gender={"Female","Male"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor__signup);
        final String[]Gender={"Female","Male"};
        first_name=(EditText)findViewById(R.id.first_name);
        last_name=(EditText)findViewById(R.id.last_name);
        user_name=(EditText)findViewById(R.id.user_name);
        password=(EditText)findViewById(R.id.password);
        file_name=(EditText)findViewById(R.id.file_name);
        confirm_password=(EditText)findViewById(R.id.confirm_password);
        email=(EditText)findViewById(R.id.email);
        country=(EditText)findViewById(R.id.country);
        city=(EditText)findViewById(R.id.city);
        phone=(EditText)findViewById(R.id.phone);
        specialization=(EditText)findViewById(R.id.special);
        gender=(Spinner)findViewById(R.id.gender);
        sign_up=(Button)findViewById(R.id.sign_up);
        upload_image=(Button)findViewById(R.id.upload_image);
        upload_cv=(Button)findViewById(R.id.upload_cv);
upload_image.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_LOAD_IMG);
    }
});
        upload_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent();
                intent.setType("application/pdf/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select CV"),RESULT_LOAD_CV);*/
               String cv_name_file= file_name.getText().toString();
        /*        try{
                File myFile = new File(Environment.getExternalStorageDirectory().getPath(),cv_name_file+".pdf");
                InputStream inputStream = new FileInputStream(myFile);

                byte[] bytes = new byte[(int) myFile.length()];
                inputStream.read(bytes);
                   cv_string = Base64.encodeToString(bytes, 0);
                    Log.d("cv in upload",cv_string);

                }
                catch(Exception e){
                    Log.d("exception", e.toString());
                }*/
                cv_string=webServices.url_upload+cv_name_file+".pdf";
                upload_file upload_file=new upload_file(Doctor_Signup.this);
                upload_file.execute(Environment.getExternalStorageDirectory().getPath()+"/"+cv_name_file+".pdf");
            }
        });
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,R.layout.gender,R.id.gendertv,Gender);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemSelectedListener(this);
        final webServices web=new webServices();
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = first_name.getText().toString();
                String lastName = last_name.getText().toString();
                String userName = user_name.getText().toString();
                String Password = password.getText().toString();
                String confirmPassword = confirm_password.getText().toString();
                String Email = email.getText().toString();
                String Country = country.getText().toString();
                String City = city.getText().toString();
                String Phone = phone.getText().toString();
                String Specialization = specialization.getText().toString();
                Log.d("s",cv_string);
                web.addDoctor(Doctor_Signup.this,firstName,lastName,userName,Password,Email,Country,City,Phone,Specialization,genderSelected,image_str,cv_string);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK ){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            String filename=imgDecodableString.substring(imgDecodableString.lastIndexOf("/")+1);
             image_str=webServices.url_upload+filename;
            upload_file upload_file=new upload_file(this);
            upload_file.execute(imgDecodableString);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genderSelected=Gender[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
