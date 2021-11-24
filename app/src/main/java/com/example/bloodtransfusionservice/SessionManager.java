package com.example.bloodtransfusionservice;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences userSessions;
    SharedPreferences.Editor editor;
    Context context;

    //Session names
    public static final String SESSION_USER_SESSION = "userLoginSession";
    public static final String SESSION_REMEMBER_ME_SESSION = "rememberMe";

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_REMEMBER_ME = "IsRememberMe";

    public static final String KEY_PHONE_NO = "phone_no";

    public static final String KEY_SESSION_PHONE_NO = "phone_no";
    public static final String KEY_SESSION_PASSWORD = "password";

    public SessionManager(Context _context, String sessionName){
        context = _context;
        userSessions = context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        editor = userSessions.edit();

    }

    public void createLoginSession(String phone_no){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_PHONE_NO,phone_no);

        editor.commit();
    }

    public void createRememberMeSession(String phone_no, String password) {
        editor.putBoolean(IS_REMEMBER_ME, true);
        editor.putString(KEY_SESSION_PHONE_NO,phone_no);
        editor.putString(KEY_SESSION_PASSWORD,password);

        editor.commit();
    }

    public HashMap<String, String> getUserDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String, String>();
        userData.put(KEY_PHONE_NO,userSessions.getString(KEY_PHONE_NO,null));

        return userData;
    }

    public HashMap<String, String> getRememberMeDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String, String>();
        userData.put(KEY_SESSION_PHONE_NO,userSessions.getString(KEY_SESSION_PHONE_NO,null));
        userData.put(KEY_SESSION_PASSWORD,userSessions.getString(KEY_SESSION_PASSWORD,null));

        return userData;
    }

    public boolean checkLogin(){
        if (userSessions.getBoolean(IS_LOGIN,true)){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkRememberMe(){
        if (userSessions.getBoolean(IS_REMEMBER_ME,true)){
            return true;
        }
        else {
            return false;
        }
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

}
