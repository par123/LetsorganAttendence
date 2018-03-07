package com.example.souravpk.attendence;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TakeAttendance extends AppCompatActivity {

    private List<Student> studentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_attendance_recycler);

        toolbar = findViewById(R.id.toolbar);

        final Calendar myCalendar = Calendar.getInstance();
        final String currentDateTime = new SimpleDateFormat("dd-MM-yyyy").format(myCalendar.getTime());
        toolbar.setTitle(currentDateTime);

        final DatePickerDialog.OnDateSetListener pickedDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(dayOfMonth, monthOfYear, year);
            }
        };

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(TakeAttendance.this, pickedDate, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        recyclerView = findViewById(R.id.attendance_recycler_view);

        studentAdapter = new StudentAdapter(studentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(studentAdapter);

        prepareStudentData();


    }

    private void updateLabel(int dayOfMonth, int monthOfYear, int year) {
        toolbar.setTitle(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
    }


    private void prepareStudentData() {
        Student student = new Student("Mad Max: Fury Road", "7/10", "70%", "Absent ");
        studentList.add(student);

        student = new Student("Inside Out", "8/10", "80%", "Present");
        studentList.add(student);

        student = new Student("Partho Protim Mondal", "8/10", "80%", "Present");
        studentList.add(student);

        student = new Student("Inside Out", "8/10", "80%", "Present");
        studentList.add(student);

        student = new Student("Gopal Roy", "8/10", "80%", "Absent ");
        studentList.add(student);

        student = new Student("Gopal Roy", "8/10", "80%", "Absent ");
        studentList.add(student);

        student = new Student("Gopal Roy", "8/10", "80%", "Absent ");
        studentList.add(student);

        student = new Student("Gopal Roy", "8/10", "80%", "Absent ");
        studentList.add(student);

        student = new Student("Gopal Roy", "8/10", "80%", "Absent ");
        studentList.add(student);

        student = new Student("Shree Nanda Lal Chandra Sarkar", "8/10", "80%", "Absent ");
        studentList.add(student);

        studentAdapter.notifyDataSetChanged();
    }
}
