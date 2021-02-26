package com.elegion.tracktor.ui.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class DefaultSettings {
    private static SharedPreferences sharedPreferences;

    // create one method that will instantiate sharedPreferecdes
    private static void getSharedPreferencesInstance(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static int savePreferences(String str, int value){
        return 1;
    }


    // for list preference
    // remember list preference will return entryValues, that means,
    // values are strings
    public static String getListPrefereceValue(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("sex", ""); // there was no default value, we can leave default value empty
    }

    // editTextPreferece [weight]
    public static String getUserWeight(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("weight", "");
    }

    // editTextPreferece [height]
    public static String getUserHeight(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("height", "");
    }


    // editTextPreferece [age]
    public static String getUserAge(Context context) {
        getSharedPreferencesInstance(context);
        return sharedPreferences.getString("age", "");
    }
}
