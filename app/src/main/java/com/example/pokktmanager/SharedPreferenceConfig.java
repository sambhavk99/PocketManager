package com.example.pokktmanager;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferenceConfig {

    private SharedPreferences sharedPreferences;
    private Context ctx;

    SharedPreferenceConfig(Context ctx){
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences(ctx.getResources().getString(R.string.login_preference),MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ctx.getResources().getString(R.string.login_status),status);
        editor.commit();
    }

    public void writeEmail(String email){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ctx.getResources().getString(R.string.username),email);
        editor.commit();
    }

    public String getEmail(){
        String email ="";
        email = sharedPreferences.getString(ctx.getResources().getString(R.string.username),email);
        return email;
    }

    public boolean readLoginStatus(){
        boolean status = false;
        status = sharedPreferences.getBoolean(ctx.getResources().getString(R.string.login_status),false);
        return  status;
    }
}
