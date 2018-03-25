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

class QueryBuilder {

    private String tableName, columnNamesInQueryFormat, columnValuesInQueryFormat, conditionClause="",
            whereUniqueClause="", updateQuery="" ;
    private int totalColumn, totalValues;
    private List<String> columnList, columnValues;
    private Context context;

    QueryBuilder(String tableName){
        this.tableName = tableName;
    }

    QueryBuilder setColumns(List<String> columnList){
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
        //Log.d("column", columnNamesInQueryFormat);

        return this;
    }

    QueryBuilder setColumnValues(List<String> valueList){
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
        //Log.d("columnValues", columnValuesInQueryFormat);

        return this;
    }

    void insert(Context context){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            for (int i=0; i < columnList.size(); i++){
                values.put(columnList.get(i), columnValues.get(i));
            }
            db.insert(tableName, null, values);
            //Log.d("insert", "inserted in "+tableName);
        }catch (Exception e){
            Log.d("error", e.toString());
        }
    }
    

    QueryBuilder where(String column, String operator, String value){
        if(conditionClause.equals("")){
            //if where() is invoked once
            this.conditionClause+= " where "+column+operator+"'"+value+"'";
        } else{
            //if where() is invoked more than once
            this.conditionClause+= " and "+column+operator+"'"+value+"'";
        }

        return this;
    }


    List<List<String>> selectAllRows(Context context){
        this.context = context;
        List<List<String>> allRows = new ArrayList<List<String>>();
        SQLiteDatabase db ;
        try {
            db = new DatabaseHelper(context).getReadableDatabase();
            String q = "select "+columnNamesInQueryFormat+ " from "+tableName+conditionClause;
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


    boolean exist(Context context){
        List<List<String>> list = selectAllRows(context);
        for (List<String> list1 : list){
            Log.d("exist", list1.get(0)+"--"+list1.get(1)+"--"+list1.get(2) );
        }

        return selectAllRows(context).size() > 0;
    }


    QueryBuilder setUpdateValues(String column, String value){
        updateQuery = "update "+tableName+" set "+column+" = "+value+" "+conditionClause;

        return this;
    }

    void update(Context context){
        SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
        //Log.d("update ", updateQuery);
        Log.d("update ", "updated");
        db.execSQL(updateQuery);
    }




}

