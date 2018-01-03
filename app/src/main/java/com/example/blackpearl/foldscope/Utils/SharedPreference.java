package com.example.blackpearl.foldscope.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreference {

    public static SharedPreferences prefrence;
    public static Editor editor;

    public static void SharedPreference(Context context) {
        // TODO Auto-generated constructor stub
        prefrence = context.getSharedPreferences("FoldScope", Context.MODE_PRIVATE);
    }

    public static String getString(String key) {

        return prefrence.getString(key, null);
    }

    public static void setString(String key, String value) {

        editor = prefrence.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void ClearAll() {

        editor = prefrence.edit();
        editor.clear();
        editor.commit();
    }
}
