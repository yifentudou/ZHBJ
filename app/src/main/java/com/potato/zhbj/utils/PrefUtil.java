package com.potato.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by li.zhirong on 2018/9/11/011 13:42
 * 对SharedPreferences的封装
 */
public class PrefUtil {

    public static String PREFERENCE_NAME = "config";
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
       sp.edit().putBoolean(key,defValue).commit();
    }

    public static String getString(Context context,String key,String defValue){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }
    public static void setString(Context context,String key,String defValue){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,defValue).commit();
    }

    public static int getInt(Context context,String key,int defValue){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }
    public static void setInt(Context context,String key,int defValue){
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,defValue).commit();
    }
}
