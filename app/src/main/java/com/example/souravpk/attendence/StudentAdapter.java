package com.example.souravpk.attendence;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sourav pk on 2/9/2018.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<Student> studentList;
    List<List<String>> studentBasicInfo;
    int courseId;
    private Context context;

    public StudentAdapter(List<Student> studentList, List<List<String>> studentBasicInfo, Context context){
        this.studentList = studentList;
        this.studentBasicInfo = studentBasicInfo;
        this.context = context;
        try {
            courseId = Integer.parseInt( studentBasicInfo.get(0).get(1) );
        }catch (Exception e){}
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_take_attendance, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.name.setText(student.getName());
        holder.rollFullForm.setText(student.getRollFullForm());
        holder.classCount.setText(student.getClassCount());
        holder.percent.setText(student.getPercent());
        holder.attendanceCheckbox.setText(student.getAttendanceStatus());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    private int n=0;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, rollFullForm, classCount, percent, tmpClassCount, tmpPercent;
        public CheckBox attendanceCheckbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            rollFullForm = itemView.findViewById(R.id.rollFullForm);
            classCount = (TextView) itemView.findViewById(R.id.classCount);
            percent = (TextView) itemView.findViewById(R.id.percent);
            attendanceCheckbox = (CheckBox) itemView.findViewById(R.id.attendanceCheckbox);

            //attendanceCheckbox

            int userId =0;
            try {
                userId = Integer.parseInt( studentBasicInfo.get(n).get(0) ); // n th row and 0 th column
            }catch (Exception e){}
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            List attendance = new DatabaseHelper(context).getAttendance(courseId, userId, date);
            try {
                if( attendance.get(0).equals("1") ){
                    designCheckbox(attendanceCheckbox, 1);
                }else {
                    designCheckbox(attendanceCheckbox, 0);
                }
                //Log.d("at",attendance.get(0).equals("1")? "1" : "0");
            }catch (Exception e){}


            classCount.setId(n);
            percent.setId(n);
            attendanceCheckbox.setId(userId);

            percent.setPadding(150,0,0,0);

            attendanceCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = attendanceCheckbox.isChecked();
                    String attendanceStatus = checked ? "Present" : "Absent";
                    DatabaseHelper databaseHelper = new DatabaseHelper(context);


                    if(attendanceStatus.equals("Absent")){
                        databaseHelper.saveAttendanceStatus(courseId, attendanceCheckbox.getId(), 0);
                        designCheckbox(attendanceCheckbox, 0);
                    }else{ //present
                        databaseHelper.saveAttendanceStatus(courseId, attendanceCheckbox.getId(), 1);
                        designCheckbox(attendanceCheckbox, 1);
                    }



                    //Log.d("atten", attendanceStatus+" for user id"+attendanceCheckbox.getId() +" in course "+courseId);
                    classCount.setText(attendanceCheckbox.getId() +"");
                    percent.setText(attendanceCheckbox.getId() +"");
                }
            });
            n++;
        }
    }

    private void designCheckbox(CheckBox attendanceCheckbox, int attendance){
        if(attendance == 1){
            attendanceCheckbox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#4cc211")));
            attendanceCheckbox.setTextColor(Color.parseColor("#338c06"));
            attendanceCheckbox.setChecked(true);
            attendanceCheckbox.setText("Present");
        }else {
            attendanceCheckbox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#c0d60101")));
            attendanceCheckbox.setTextColor(Color.parseColor("#ff0000"));
            attendanceCheckbox.setChecked(false);
            attendanceCheckbox.setText("Absent");
        }
    }
}
