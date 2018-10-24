package com.example.souravpk.attendence;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sourav pk on 2/10/2018.
 */

public class ApiCaller extends AsyncTask<String, Void, String> {

    public ApiCaller_Callback delegate;
    public String serverResponse;
    private String requestType, requestUrl;

    public ApiCaller(String requestType, ApiCaller_Callback delegate){
        this.requestType = requestType;
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        String inputLine;
        URL myUrl = null;
        try {
            myUrl = new URL("http://choriyedao.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(requestType.equals("getCourseData")){
            String email = strings[0];
            String password = strings[1];
            //Create a URL object holding our url
            try {
                myUrl = new URL("http://choriyedao.com/api/attendance/get_basic_data?email="+email+"&password="+password);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if(requestType.equals("syncData")){
            String data = strings[0];
            Log.d("sync", "should call api");
            try {
                try {
                    myUrl = new URL("http://choriyedao.com/api/attendance/send_attendance_data?data="+ URLEncoder.encode(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            response = "success";
        }

        try {

            //Create a connection
            HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
            //Set methods and timeouts
            connection.setRequestMethod("GET");

            //Connect to our url
            connection.connect();
            //Create a new InputStreamReader
            InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            //Check if the line we are reading is not null
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }
            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();
            //Set our result equal to our stringBuilder
            response = stringBuilder.toString();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("doIn", response);

        return response;
    }


    @Override
    protected void onPostExecute(String s) {
        //super.onPostExecute(s);

//        try {
//            JSONObject object = new JSONObject(s);
//            JSONArray Jarray  = object.getJSONArray("courses");
//
//            for (int i = 0; i < Jarray.length(); i++)
//            {
//                JSONObject course = Jarray.getJSONObject(i);
//
//                Log.d("courses", course.getString("course_name"));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        serverResponse = s;
        delegate.getServerResponse(s);

        //Log.d("onPost", s);
    }
}
