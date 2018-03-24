package com.example.souravpk.attendence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourav pk on 3/6/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String dbName = "attendance";
    private static final int dbVersion = 1;

    private Context context;

    private QueryBuilder queryBuilder;


    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q = "create table credentials(id integer primary key, email string, password string)";
        sqLiteDatabase.execSQL(q);

        q = "create table my_courses(id integer primary key, course_id integer unique, course_code string, course_name string, institute string, dept string)";
        sqLiteDatabase.execSQL(q);

        q = "create table student_basic_info(id integer primary key, user_id integer, course_id integer, roll_numeric integer, roll_full_form string, name string not null default '')";
        //course_id is unique in server database
        sqLiteDatabase.execSQL(q);


        q = "create table student_attendance (id integer primary key, user_id integer, date string, time string, attendance integer not null default 1)";
        sqLiteDatabase.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+"credentials");
    }



    public boolean userExist(String userId){
        List columnList = new ArrayList();
        columnList.add("id");
        columnList.add("email");
        columnList.add("password");
        List valueList = new ArrayList();
        valueList.add(userId);
        List user = new QueryBuilder("credentials")
                .setColumns(columnList)
                .setColumnValues(valueList)
                .fetchData();

        Log.d("mylog", user.toString());

        return user.size() == 1 ;
    }


    public boolean userLoggedIn() {
        List columnList = new TableColumns("credentials").prepareListOf("id");
        queryBuilder = new QueryBuilder("credentials");
        List<List<String>> allRows = queryBuilder.setColumns(columnList).selectAllRows(context);

        for (List<String> row : allRows){
            
        }
        Log.d("row", String.valueOf(allRows.size()));


        return allRows.size() > 0;
    }

    public void saveCredentials(String email, String password, String response) {
        try {
            JSONObject object = new JSONObject(response);
            String userId  = object.getString("userId");
            Log.d("uid", userId);
            List<String> columnList = new TableColumns("credentials").prepareListOf("id", "email", "password");
            List<String> columnValueList = new TableColumns("credentials").prepareListOf(userId, email, password);
            new QueryBuilder("credentials")
                    .setColumns(columnList)
                    .setColumnValues(columnValueList)
                    .insert(context);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void saveCourses(String response) {
        QueryBuilder queryBuilder = new QueryBuilder("my_courses");
        try {
            JSONObject object = new JSONObject(response);
            JSONArray Jarray  = object.getJSONArray("courses");

            for (int i = 0; i < Jarray.length(); i++)
            {
                JSONObject course = Jarray.getJSONObject(i);
                List<String> columns = new TableColumns("my_courses").prepareListOf("course_id", "course_code", "course_name", "institute", "dept");
                List<String> columnValues = new TableColumns("my_courses").prepareListOf(course.getString("course_id"), course.getString("course_code"), course.getString("course_name"), course.getString("institute"), course.getString("dept"));
                queryBuilder.setColumns(columns).setColumnValues(columnValues).insert(context);
                //Log.d("courses", course.getString("course_id")+"--"+course.getString("course_name")+"--"+course.getString("course_code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveStudentBasicInfo(String response){
        QueryBuilder queryBuilder = new QueryBuilder("student_basic_info");
        try{
            JSONObject object = new JSONObject(response);
            JSONArray jsonArray = object.getJSONArray("courseTakerStudents");
            int len = jsonArray.length();
            for (int i=0; i < len; i++){
                JSONObject student = jsonArray.getJSONObject(i);
                String userId = student.getString("user_id");
                String courseId = student.getString("course_id");
                String rollNumeric = student.getString("roll_numeric");
                String rollFullForm = student.getString("roll_full_form");
                String name = student.getString("name");
                Log.d("roll", rollFullForm);
                List<String> columns = new TableColumns("student_basic_info").prepareListOf("user_id","course_id","roll_numeric","roll_full_form","name");
                List<String> values = new TableColumns("student_basic_info").prepareListOf(userId, courseId, rollNumeric, rollFullForm, name);
                queryBuilder.setColumns(columns).setColumnValues(values).insert(context);
            }
        }catch (Exception e){
            Log.d("error", e.toString()+" >> saveStudentBasicInfo()");
        }
    }



}
