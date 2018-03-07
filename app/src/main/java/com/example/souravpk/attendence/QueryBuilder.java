package com.example.souravpk.attendence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourav pk on 3/6/2018.
 */

public class QueryBuilder {

    private String tableName, columnNamesInQueryFormat, columnValuesInQueryFormat ;
    private int totalColumn, totalValues;
    private List<String> columnList, columnValues;

    public QueryBuilder(String tableName){
        this.tableName = tableName;
    }

    public QueryBuilder setColumns(List<String> columnList){
        this.columnList = columnList;
        totalColumn = columnList.size();
        String buildStr = "";
        for (int i=0; i < totalColumn; i++){
            if(i == totalColumn-1 ){
                buildStr += columnList.get(i);
            }else{
                buildStr += columnList.get(i) +",";
            }
        }
        columnNamesInQueryFormat = buildStr;
        Log.d("column", columnNamesInQueryFormat);

        return this;
    }

    public QueryBuilder setColumnValues(List<String> valueList){
        this.columnValues = valueList;
        totalValues = valueList.size();
        String buildStr = "";
        for (int i=0; i < totalValues; i++){
            if(i == totalValues-1 ){
                buildStr += valueList.get(i);
            }else{
                buildStr += valueList.get(i) +",";
            }
        }
        columnValuesInQueryFormat = buildStr;
        Log.d("columnValues", columnValuesInQueryFormat);

        return this;
    }

    public void insert(Context context){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            for (int i=0; i < columnList.size(); i++){
                values.put(columnList.get(i), columnValues.get(i));
            }
            db.insert(tableName, null, values);
            Log.d("insert", "inserted in "+tableName);
        }catch (Exception e){
            Log.d("error", e.toString());
        }
    }

    public List<String> fetchData(){
        List<String> data = new ArrayList<>();

        return  data;
    }

    public List<List<String>> selectAllRows(Context context){
        List<List<String>> allRows = new ArrayList<List<String>>();
        SQLiteDatabase db ;
        try {
            db = new DatabaseHelper(context).getReadableDatabase();
            String q = "select "+columnNamesInQueryFormat+ " from "+tableName;
            Log.d("q", q);
            Cursor cursor = db.rawQuery(q, null);
            if( cursor != null ){
                while (cursor.moveToNext()){
                    List<String> row = new ArrayList<>();
                    for (int i=0; i < totalColumn; i++){
                        row.add(cursor.getString(i));
                    }
                    allRows.add(row);
                }
            }
        }catch (Exception e){
            Log.d("error", e.toString());
        }
        return  allRows;
    }


}

