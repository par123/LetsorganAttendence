package com.example.souravpk.attendence;

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

import java.util.List;

/**
 * Created by sourav pk on 2/9/2018.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<Student> studentList;
    List<List<String>> studentBasicInfo;
    int courseId;

    public StudentAdapter(List<Student> studentList, List<List<String>> studentBasicInfo){
        this.studentList = studentList;
        this.studentBasicInfo = studentBasicInfo;
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

            int userId = Integer.parseInt( studentBasicInfo.get(n).get(0) );

            classCount.setId(n);
            percent.setId(n);
            attendanceCheckbox.setId(userId);

            percent.setPadding(150,0,0,0);

            attendanceCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = attendanceCheckbox.isChecked();
                    String attendanceStatus = checked ? "Present" : "Absent";
                    if(attendanceStatus.equals("Absent")){
                        attendanceCheckbox.setBackgroundColor(Color.parseColor("#c0d60101"));
                        attendanceCheckbox.setTextColor(Color.parseColor("#ffffff"));
                    }else{
                        attendanceCheckbox.setBackgroundColor(Color.parseColor("#1ba99a34"));
                        attendanceCheckbox.setTextColor(Color.parseColor("#338c06"));
                    }

                    

                    Log.d("atten", attendanceStatus+" for user id"+attendanceCheckbox.getId() +" in course "+courseId);
                    classCount.setText(attendanceCheckbox.getId() +"");
                    percent.setText(attendanceCheckbox.getId() +"");
                    attendanceCheckbox.setText(attendanceStatus);
                }
            });
            n++;
        }
    }
}
