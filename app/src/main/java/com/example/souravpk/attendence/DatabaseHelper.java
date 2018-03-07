package com.example.souravpk.attendence;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sourav pk on 3/6/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String dbName = "attendance";
    private static final int dbVersion = 1;

    private Context context;

    private QueryBuilder queryBuilder;


    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q = "create table credentials(id integer primary key, email string, password string)";
        sqLiteDatabase.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+"credentials");
    }



    public boolean userExist(String userId){
        List columnList = new ArrayList();
        columnList.add("id");
        columnList.add("email");
        columnList.add("password");
        List valueList = new ArrayList();
        valueList.add(userId);
        List user = new QueryBuilder("credentials")
                .setColumns(columnList)
                .setColumnValues(valueList)
                .fetchData();

        Log.d("mylog", user.toString());

        return user.size() == 1 ;
    }


    public boolean userLoggedIn() {
        List columnList = new TableColumns("credentials").prepareListOf("id");
        queryBuilder = new QueryBuilder("credentials");
        List<List<String>> allRows = queryBuilder.setColumns(columnList).selectAllRows(context);

        for (List<String> row : allRows){
            
        }
        Log.d("row", String.valueOf(allRows.size()));


        return allRows.size() > 0;
    }


}
