package com.example.souravpk.attendence;

/**
 * Created by sourav pk on 2/9/2018.
 */

public class Student {

    private String name, rollFullForm, classCount, percent, attendanceStatus;

    public Student(){

    }

    public Student(String name, String rollFullForm, String classCount, String percent, String attendanceStatus){
        this.name = name;
        this.rollFullForm = rollFullForm;
        this.classCount = classCount;
        this.percent = percent;
        this.attendanceStatus = attendanceStatus;
    }

    public String getName() {
        return name;
    }

    public String getRollFullForm(){
        return rollFullForm;
    }

    public String getClassCount() {
        return classCount;
    }

    public String getPercent() {
        return percent;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollFullForm(String rollFullForm) {
        this.rollFullForm = rollFullForm;
    }

    public void setClassCount(String classCount) {
        this.classCount = classCount;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
