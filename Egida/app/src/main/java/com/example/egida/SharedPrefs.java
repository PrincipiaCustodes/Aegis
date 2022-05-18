package com.example.egida;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class SharedPrefs {
    private final static String PREFS_FILE_NAME = "PreferencesFile";
    public final static String PASSWORD = "password";
    public final static String NICKNAME = "nickname";
    public final static String BIOMETRICS_STATUS = "biometrics_status";
    public final static String SIGNUP_STATUS = "signup_status";
    public final static String FIRST_START = "is_first_start";

    public static void setPASSWORD(Context currentContext, String password) throws NoSuchAlgorithmException {
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PASSWORD, new ShaEncoder(password).sha256EncodeInput());
        editor.apply();
        Toast.makeText(currentContext.getApplicationContext(), currentContext.getString(R.string.password_updated),
                Toast.LENGTH_SHORT).show();
    }

    public static String getPASSWORD(Context currentContext){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(PASSWORD, "password_error");
    }

    public static void setNICKNAME(Context currentContext, String nickname){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NICKNAME, nickname);
        editor.apply();
        Toast.makeText(currentContext.getApplicationContext(), currentContext.getString(R.string.nickname_updated),
                Toast.LENGTH_SHORT).show();
    }

    public static String getNICKNAME(Context currentContext){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(NICKNAME, "nickname_error");
    }

    public static void setBIOMETRICS_STATUS(Context currentContext, String biometricsStatus){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(BIOMETRICS_STATUS, biometricsStatus);
        editor.apply();
        Toast.makeText(currentContext.getApplicationContext(), currentContext.getString(R.string.biometrics_updated),
                Toast.LENGTH_SHORT).show();
    }

    public static String getBIOMETRICS_STATUS(Context currentContext){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(BIOMETRICS_STATUS, "biometrics_status_error");
    }

    public static void setSIGNUP_STATUS(Context currentContext, String signUpStatus){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SIGNUP_STATUS, signUpStatus);
        editor.apply();
        Toast.makeText(currentContext.getApplicationContext(), currentContext.getString(R.string.sign_up_status),
                Toast.LENGTH_SHORT).show();
    }

    public static String getSIGNUP_STATUS(Context currentContext){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(SIGNUP_STATUS, "signup_status_error");
    }

    public static void setFIRST_START(Context currentContext, boolean isFirstStart){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FIRST_START, isFirstStart);
        editor.apply();
    }

    public static Boolean getFIRST_START(Context currentContext){
        SharedPreferences sharedPref = currentContext.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(FIRST_START, false);
    }
}
