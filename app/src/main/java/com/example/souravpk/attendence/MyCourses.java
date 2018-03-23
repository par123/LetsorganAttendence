package com.example.souravpk.attendence;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyCourses extends AppCompatActivity {

    private QueryBuilder queryBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        queryBuilder = new QueryBuilder("my_courses");
        List<String> columns = new TableColumns("my_courses").prepareListOf("course_id", "course_code", "course_name", "institute", "dept");
        List<List<String>> courses = queryBuilder
                .setColumns(columns)
                .selectAllRows(getApplicationContext());


        LinearLayout MainLL= (LinearLayout) findViewById(R.id.my_courses_LL);
        for(int i=0; i<15; i++){

        }

        for (List<String> course : courses){
            int courseId = Integer.parseInt(course.get(0));
            String courseCode = course.get(1);
            String courseName = course.get(2);
            String institute = course.get(3);
            String dept = course.get(4);

            String textToShow = "<font color='#e25b00'>"+courseName+"</font> <font color='#782'> ( "+courseCode+" )</font> <br><br>"+dept+"<br>"+institute;

            final TextView text = new TextView(this);
            text.setText(Html.fromHtml(textToShow));
            text.setTextSize(12);
            text.setGravity(Gravity.LEFT);
            text.setPadding(25,50,25,50);
            text.setTextSize(18);
            text.setBackgroundResource(R.drawable.bg1);
            text.setTag(courseId);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,60,0,0);
            text.setLayoutParams(params);
            MainLL.addView(text);


            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(MyCourses.this, text.getTag()+"", Toast.LENGTH_SHORT).show();
                    //finish();
                    Intent intent = new Intent(getApplicationContext(), TakeAttendance.class);
                    intent.putExtra("course_id", text.getTag()+"");
                    startActivity(intent);
                }
            });
        }

    }


    public void setOnClickListener(final int position, TextView... arrayOfView) {

        TextView[] textViewArray = arrayOfView;

        for (int i = 0; i < textViewArray.length; ++i) {

            int index = i;
            textViewArray[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int textViewNumber = position;
                    Toast.makeText(MyCourses.this, textViewNumber+" th TV", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
