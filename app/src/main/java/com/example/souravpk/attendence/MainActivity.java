package com.example.souravpk.attendence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Library library;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        if( databaseHelper.userLoggedIn() ){
            Log.d("login", "y1");
            finish();
            startActivity(new Intent(MainActivity.this, MyCourses.class));
        }


        final EditText emailField = (EditText) findViewById(R.id.email);
        final EditText passwordField = (EditText) findViewById(R.id.pass);


        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = String.valueOf(emailField.getText());
                final String password = String.valueOf(passwordField.getText());

                library = new Library();
                if(library.isNetworkAvailable(MainActivity.this)){
                    ApiCaller apiCaller;
                    final String finalEmail = email;
                    apiCaller = new ApiCaller("getCourseData", new ApiCaller_Callback() {
                        @Override
                        public void getServerResponse(String response) {
                            Log.d("response", response);
                            if( valid(response) ){
                                Log.d("login", "y2");
                                databaseHelper = new DatabaseHelper(MainActivity.this);
                                databaseHelper.saveCredentials(email, password, response);
                                databaseHelper.saveCourses(response);
                                databaseHelper.saveStudentBasicInfo(response);
                                finish();
                                startActivity(new Intent(MainActivity.this, MyCourses.class));
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Wrong Email or password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    apiCaller.execute(email, password);
                }
                else{
                    Toast.makeText(MainActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private boolean valid(String response) {
        boolean valid = true;
        try {
            JSONObject object = new JSONObject(response);
            String userId  = object.getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }


}


