package com.example.souravpk.attendence;

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

    public StudentAdapter(List<Student> studentList){
        this.studentList = studentList;
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
        public TextView name, classCount, percent, tmpClassCount, tmpPercent;
        public CheckBox attendanceCheckbox;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            classCount = (TextView) itemView.findViewById(R.id.classCount);
            percent = (TextView) itemView.findViewById(R.id.percent);
            attendanceCheckbox = (CheckBox) itemView.findViewById(R.id.attendanceCheckbox);


            classCount.setId(n);
            percent.setId(n);
            attendanceCheckbox.setId(n);

            percent.setPadding(150,0,0,0);

            attendanceCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = attendanceCheckbox.isChecked();
                    String attendanceStatus = checked ? "Present " : "Absent ";
                    Log.d("atten", attendanceStatus);
                    classCount.setText(attendanceCheckbox.getId() +"");
                    percent.setText(attendanceCheckbox.getId() +"");
                    attendanceCheckbox.setText(attendanceStatus);
                }
            });
            n++;
        }
    }
}
