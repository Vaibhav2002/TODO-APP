package com.example.todo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class RememberMeSession {
    final static String ISREMEMBERME="RememberMe";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    final static String USERNAME="username";
    final static String PASSWORD="password";
    Context context;

    RememberMeSession(Context _context)
    {
        context=_context;
        sharedPreferences=context.getSharedPreferences("REmemberMesession",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    void createRememberMeSession(String username,String password)
    {
        editor.putBoolean(ISREMEMBERME,true);
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        editor.commit();
    }
    boolean checkRememberMe()
    {
        return sharedPreferences.getBoolean(ISREMEMBERME, false);
    }
    HashMap<String,String> returnData()
    {
        HashMap<String,String > user =new HashMap<>();
        user.put(RememberMeSession.USERNAME,sharedPreferences.getString(USERNAME,null));
        user.put(RememberMeSession.PASSWORD,sharedPreferences.getString(PASSWORD,null));
        return user;
    }
    void removeRememberMe()
    {
        editor.clear();
        editor.commit();
    }
}
