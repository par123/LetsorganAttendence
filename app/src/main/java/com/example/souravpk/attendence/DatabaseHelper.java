package com.example.souravpk.attendence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    DatabaseHelper(Context context){
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


        q = "create table student_attendance (id integer primary key, course_id integer, user_id integer, date string, attendance integer not null default 1)";
        sqLiteDatabase.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+"credentials");
    }



    boolean userLoggedIn() {
        List columnList = new TableColumns("credentials").prepareListOf("id");
        queryBuilder = new QueryBuilder("credentials");
        List<List<String>> allRows = queryBuilder.setColumns(columnList).selectAllRows(context);

        for (List<String> row : allRows){

        }
        //Log.d("row", String.valueOf(allRows.size()));


        return allRows.size() > 0;
    }

    void saveCredentials(String email, String password, String response) {
        try {
            JSONObject object = new JSONObject(response);
            String userId  = object.getString("userId");
            //Log.d("uid", userId);
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


    void saveCourses(String response) {
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

    void saveStudentBasicInfo(String response){
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


    void saveAttendanceStatus(int courseId, int userId, int attendanceStatus) {
        QueryBuilder queryBuilder = new QueryBuilder("student_attendance");
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        List columns = new TableColumns("student_attendance")
                .prepareListOf("course_id", "user_id", "date");
        List values = new TableColumns("student_attendance")
                .prepareListOf(String.valueOf(courseId), String.valueOf(userId), date);

        boolean exist = queryBuilder.setColumns(columns).setColumnValues(values)
                .where("course_id", "=", String.valueOf(courseId))
                .where("user_id", "=", String.valueOf(userId))
                .where("date", "=", date)
                .exist(context);

        if(exist){
            Log.d("exist", "this data exist");
            queryBuilder = new QueryBuilder("student_attendance");
            queryBuilder.where("user_id", "=", String.valueOf(userId))
                    .where("course_id", "=", String.valueOf(courseId))
                    .where("date", "=", date)
                    .setUpdateValues("attendance", String.valueOf(attendanceStatus))
                    .update(context);
        }else{
            columns = new TableColumns("student_attendance").prepareListOf("course_id", "user_id", "date", "attendance");
            values = new TableColumns("student_attendance").prepareListOf(String.valueOf(courseId), String.valueOf(userId), date, String.valueOf(attendanceStatus));
            queryBuilder.setColumns(columns).setColumnValues(values).insert(context);

            Log.d("exist", courseId+" "+userId+" "+date+" inserted");
        }
    }

    List getAttendance(int courseId, int userId, String date) {
        List data = new ArrayList();
        QueryBuilder queryBuilder = new QueryBuilder("student_attendance");
        List columns = new TableColumns("student_attendance").prepareListOf("attendance");
        List<List<String>> list = queryBuilder.setColumns(columns)
                .where("course_id", "=", String.valueOf(courseId))
                .where("user_id", "=", String.valueOf(userId))
                .where("date", "=", date)
                .selectAllRows(context);

        try {
            data.add(list.get(0).get(0));
        }catch (Exception e){}



        for (List list1 : list){
            //Log.d("at", list1.get(0)+"");
        }

        return data;
    }

    public int getTotalClass(String currentDate) {
        int total = 0;
        QueryBuilder queryBuilder = new QueryBuilder("student_attendance");
        List<String> columnList = new TableColumns("student_attendance").prepareListOf("distinct date");
        total = queryBuilder.setColumns(columnList).selectAllRows(context).size();

        return total;
    }

    public int getNumOfPresentedClass(String courseId, String userId) {
        int p = 0;
        QueryBuilder queryBuilder = new QueryBuilder("student_attendance");
        List<String> columnList = new TableColumns("student_attendance").prepareListOf("course_id", "user_id", "attendance");
        p = queryBuilder.setColumns(columnList)
                .where("course_id", "=", courseId)
                .where("user_id", "=", userId)
                .where("attendance", "=", "1")
                .selectAllRows(context).size();

        return p;
    }

    void presentAllStudents(String courseId) {
        queryBuilder = new QueryBuilder("student_attendance");
        List<String> columnList = new TableColumns("student_attendance").prepareListOf("date");
        queryBuilder.setColumns(columnList).where("date", "=", new Library().getTodayDate());
        boolean existTodaysAttendance = queryBuilder.exist(context);

        if(! existTodaysAttendance){
            columnList = new TableColumns("student_basic_info").prepareListOf("user_id");
            List<List<String>> studentTakenThisCourse = queryBuilder.setColumns(columnList).where("course_id", "=", courseId).selectAllRows(context);
            Log.d("count", studentTakenThisCourse.size()+"");
            for (List user:studentTakenThisCourse){
                int userId = Integer.parseInt( user.get(0).toString() );
                saveAttendanceStatus(Integer.parseInt(courseId),userId,1);
            }
        }
    }

    public String getAttendance(String courseId) {
        List<List<String>> list;

        QueryBuilder queryBuilder = new QueryBuilder("student_attendance");
        List<String> columnList = new TableColumns("student_attendance").prepareListOf("course_id", "user_id", "date", "attendance");
        list = queryBuilder.setColumns(columnList).where("course_id", "=", courseId).selectAllRows(context);

        String jsonStr = "";
        for (List row : list){
            String course_id = row.get(0).toString();
            String user_id = row.get(1).toString();
            String attendance_date = row.get(2).toString();
            String attendance_status = row.get(3).toString();

            jsonStr+= "{\"course_id\":\""+course_id+"\",\"user_id\":\""+user_id+"\",\"attendance_date\":\""+attendance_date+"\",\"attendance_status\":\""+attendance_status+"\"},";
        }
        jsonStr = removeLastChar(jsonStr); // remove last comma(,)
        jsonStr = "["+jsonStr+"]";
        //Log.d("row", jsonStr);

        return jsonStr;
    }

    public void syncData(String response) {
        Log.d("db syncData", "syncData() invoked");
    }


    private String removeLastChar(String str) {
        String retVal = "";
        try {
            retVal = str.substring(0, str.length() - 1);
        }catch (Exception e){
        }
        return retVal;
    }
}
