package com.example.souravpk.attendence;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindorks.placeholderview.PlaceHolderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.List;

public class MyCourses extends AppCompatActivity {

    private QueryBuilder queryBuilder;

    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;

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
            text.setPadding(25,30,25,30);
            text.setTextSize(18);
            text.setBackgroundResource(R.drawable.bg1);
            text.setTag(courseId);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,30,0,0);
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


        final TextView text = new TextView(this);
        text.setText(Html.fromHtml("Synchronise to Server"));
        text.setTextSize(12);
        text.setGravity(Gravity.CENTER);
        text.setPadding(15,15,15,15);
        text.setTextSize(23);
        text.setBackgroundResource(R.drawable.bg2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(100,100,100,100);
        text.setLayoutParams(params);
        MainLL.addView(text);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyCourses.this, "Synchronizing....", Toast.LENGTH_LONG).show();
                ApiCaller apiCaller;
                apiCaller = new ApiCaller("syncData", new ApiCaller_Callback() {
                    @Override
                    public void getServerResponse(String response) {
                        Log.d("sync response", response);
                        if( valid(response) ){
                            Log.d("sync data", "y2");
                            DatabaseHelper databaseHelper = new DatabaseHelper(MyCourses.this);
                            databaseHelper.syncData(response);
                            Toast.makeText(MyCourses.this, "Data update successful !", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MyCourses.this, "Couldn't sync.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                String attendanceData = databaseHelper.getAttendance();
                Log.d("attendance", attendanceData);

                apiCaller.execute(attendanceData);

//                JSONArray jsonArray = new JSONArray();
//                for(List list : attendanceOfSingleCourse){
//                    JSONArray newArray = new JSONArray(list);
//                    jsonArray.put(newArray);
//                }
//                Log.d("json", jsonArray.toString());
            }
        });




        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
        //setupDrawer();




    }


    public void setOnClickListener(final int position, TextView... arrayOfView) {

        TextView[] textViewArray = arrayOfView;

        for (int i = 0; i < textViewArray.length; ++i) {

            int index = i;
            textViewArray[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int textViewNumber = position;
                    //Toast.makeText(MyCourses.this, textViewNumber+" th TV", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    /* drawer */
    private void setupDrawer() {
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_REQUESTS))
//                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_MESSAGE))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_GROUPS))
//                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIFICATIONS))
//                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TERMS))
//                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS))
//                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT))
        ;

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.d("menu", "closed");
                super.onDrawerClosed(drawerView);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://letsorgan.com/generatedPDF/38.pdf"));
                startActivity(browserIntent);
            }
        };

        //mDrawer.addDrawerListener(drawerToggle);
        //drawerToggle.syncState();
    }




    /* drawer */



    private boolean valid(String response) {
        boolean valid = false;
        try {
            JSONObject object = new JSONObject(response);
            String syncStatus  = object.getString("syncStatus");
            if(syncStatus.equals("success")){
                valid = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        valid = true;
        return valid;
    }


}
