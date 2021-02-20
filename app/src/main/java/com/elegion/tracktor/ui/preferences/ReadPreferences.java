package com.elegion.tracktor.ui.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.elegion.tracktor.App;

import javax.inject.Inject;

import toothpick.Toothpick;

public class ReadPreferences{
    @Inject
    static
    Context mContext;

    public ReadPreferences() {

        Toothpick.inject(this, App.getAppScope());

}

    public static void loadSharedPreferences()  {

//https://stackoverflow.com/questions/50613619/using-the-toothpick-di-framework-be-used-with-a-java-only-project
//https://github.com/WarrenFaith/Toothpick-Sample

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        String username = prefs.getString("username", "Default NickName");
        String passw = prefs.getString("password", "Default Password");
        boolean checkBox = prefs.getBoolean("checkBox", false);
        String listPrefs = prefs.getString("listpref", "Default list prefs");
    }

}
