/**
 * Created by vishal on 26/9/16.
 */

package com.showbuddy4.Utils;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceHelper {


//small changes
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public PreferenceHelper(Context ctx, String FileName) {
        prefs = ctx.getSharedPreferences(FileName, Context.MODE_PRIVATE);
    }

    public void clearAllPrefs() {
        prefs.edit().clear().apply();
    }

    public void initPref() {
        editor = prefs.edit();
    }

    public void ApplyPref() {
        editor.apply();
    }

    public void SaveStringPref(String key, String value) {
        editor.putString(key, value);
    }

    public String LoadStringPref(String key, String DefaultValue) {
        return prefs.getString(key, DefaultValue);
    }

    public void removestring(String str) {
        prefs.edit().remove(str).apply();
    }
    public void SaveIntPref(String key, int value) {
        editor.putInt(key, value);
    }

    public int LoadIntPref(String key, int DefaultValue) {
        return prefs.getInt(key, DefaultValue);
    }

    public void SaveBooleanPref(String key, boolean value) {
        editor.putBoolean(key, value);
    }

    public boolean LoadBooleanPref(String key, boolean DefaultValue) {
        return prefs.getBoolean(key, DefaultValue);
    }

}