package com.jc.geochat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.EditText;

public class Utils {

    public static String getUserNameFromPreferences(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String userName = prefs.getString(Constants.KEY_USER_NAME, "User Name");

        return userName;
    }

    public static void saveToPreferences(Context context,String userName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.KEY_USER_NAME,userName);
        editor.apply();
    }
}
