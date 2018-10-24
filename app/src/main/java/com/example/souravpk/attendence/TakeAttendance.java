package com.example.souravpk.attendence;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TakeAttendance extends AppCompatActivity {

    private List<Student> studentList = new ArrayList<>();
    private StudentAdapter studentAdapter;
    List<List<String>> studentBasicInfo;
    Toolbar toolbar;
    String courseId, currentDate;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance_recycler);










        toolbar = findViewById(R.id.toolbar);

        final Calendar myCalendar = Calendar.getInstance();
        currentDate = new SimpleDateFormat("dd-MM-yyyy").format(myCalendar.getTime());
        toolbar.setTitle(currentDate);



        final DatePickerDialog.OnDateSetListener pickedDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dayOfMonth, monthOfYear, year);
            }
        };

//        toolbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new DatePickerDialog(TakeAttendance.this, pickedDate, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });

        RecyclerView recyclerView = findViewById(R.id.attendance_recycler_view);


        courseId = getIntent().getStringExtra("course_id");
        //Toast.makeText(TakeAttendance.this, courseId+" course id", Toast.LENGTH_SHORT).show();
        initializeToday(courseId);

        QueryBuilder queryBuilder = new QueryBuilder("student_basic_info");
        List<String> columns = new TableColumns("student_basic_info").prepareListOf("user_id","course_id","roll_numeric","roll_full_form","name");
        studentBasicInfo = queryBuilder.setColumns(columns)
                .where("course_id", "=", courseId)
                .selectAllRows(getApplicationContext());
        //Log.d("count", studentBasicInfo.size()+" for course id "+courseId);

        studentAdapter = new StudentAdapter(studentList, studentBasicInfo, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(studentAdapter);

        prepareStudentData();



        columns = new TableColumns("student_attendance").prepareListOf("course_id", "user_id", "date", "attendance");
        queryBuilder = new QueryBuilder("student_attendance");
        List<List<String>> rows = queryBuilder.setColumns(columns).selectAllRows(getApplicationContext());
        for (List row : rows){
            //Log.d("row", row.get(0)+" > "+row.get(1)+" > "+row.get(2)+" > "+row.get(3));
        }
    }

    private void initializeToday(String courseId) {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.presentAllStudents(courseId);
    }

    private void updateLabel(int dayOfMonth, int monthOfYear, int year) {
        toolbar.setTitle(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
    }


    private void prepareStudentData() {

        Student student;

        for (List<String> studentInfo : studentBasicInfo){
            String name = String.valueOf(studentInfo.get(4));
            String userId = String.valueOf(studentInfo.get(0));
            String rollFullForm = String.valueOf(studentInfo.get(3));
            String attendanceText="Present";

            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            List attendance = new DatabaseHelper(getApplicationContext()).getAttendance(Integer.parseInt(courseId), Integer.parseInt(userId), date);
            try{
                if( attendance.get(0).equals("1") ){
                    attendanceText = "Present";
                }else {
                    attendanceText = "Absent";
                }
            }catch (Exception e){}

            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

            int totalClass = databaseHelper.getTotalClass(currentDate);
            int presentedClass = databaseHelper.getNumOfPresentedClass(courseId, userId);
            //Log.d("presented class", presentedClass+"/"+totalClass+" for uid "+userId+" in course "+courseId);

            student = new Student(name, rollFullForm, "7/10", "70%", attendanceText);
            studentList.add(student);
        }

        studentAdapter.notifyDataSetChanged();
    }
}
