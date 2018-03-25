package com.example.souravpk.attendence;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourav pk on 3/6/2018.
 */

public class TableColumns {

    private String tableName;

    public TableColumns(String tableName){
        this.tableName = tableName;
    }

    public List prepareListOf(String... columnNames){
        List list = new ArrayList();
        for (int i=0; i < columnNames.length ; i++){
            list.add(columnNames[i]);
            //Log.d("columnName", columnNames[i]);
        }


        return list;
    }
}
