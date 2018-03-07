package com.example.souravpk.attendence;

import android.util.Log;

/**
 * Created by sourav pk on 2/10/2018.
 */

public class LoginVerifier {

    private String email, password;
    private ApiCaller apiCaller;
    private boolean valid = false;

    public LoginVerifier(String email, String password){
        this.email = email;
        this.password = password;

        //processCredentials();
    }

    public void processCredentials(){
//        apiCaller = new ApiCaller("getCourses", new ApiCaller_Callback() {
//            @Override
//            public void getServerResponse(String response) {
//                Log.d("response", response);
//                //valid = response.equals(1) ? true : false;
//            }
//        });
//
//        apiCaller.execute(email, password);
    }
}
