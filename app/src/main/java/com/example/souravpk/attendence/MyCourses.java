package com.example.souravpk.attendence;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyCourses extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);



        LinearLayout MainLL= (LinearLayout) findViewById(R.id.my_courses_LL);
        for(int i=0; i<15; i++){
            final TextView text = new TextView(this);
            text.setText("The Value of i is :"+i); // <-- does it really compile without the + sign?
            text.setTextSize(12);
            text.setGravity(Gravity.LEFT);
            text.setPadding(25,50,25,50);
            text.setTextSize(18);
            text.setBackgroundResource(R.drawable.bg1);
            text.setTag(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,60,0,0);
            text.setLayoutParams(params);
            MainLL.addView(text);


            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MyCourses.this, text.getTag()+"", Toast.LENGTH_SHORT).show();
                    //finish();
                    startActivity(new Intent(getApplicationContext(), TakeAttendance.class));
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
