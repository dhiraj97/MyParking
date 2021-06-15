package com.example.parkmania.session_manager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.parkmania.models.Users;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setValues(String useid) {
        prefs.edit().putString("userid", useid).commit();
        prefs.edit().putBoolean("islogin", true).commit();
    }

    public String getUserId() {
        return prefs.getString("userid","");
    }

    public boolean isIsLogin()
    {
        return prefs.getBoolean("islogin",false);
    }

    public void logOut()
    {
        prefs.edit().putBoolean("islogin", false).commit();
        prefs.edit().putString("userid", "").commit();
    }

}