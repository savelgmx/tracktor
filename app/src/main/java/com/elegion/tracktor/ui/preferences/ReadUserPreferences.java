package com.elegion.tracktor.ui.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.elegion.tracktor.App;

import javax.inject.Inject;

import toothpick.Toothpick;

public class ReadUserPreferences implements UserRepository{

    private final SharedPreferences sharedPreferences;

    @Inject
    public ReadUserPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getListPreferenceValue(Context context) {
        return null;
    }

    @Override
    public String getUserWeight(Context context) {
        return null;
    }

    @Override
    public String getUserHeight(Context context) {
        return null;
    }

    @Override
    public String getUserAge(Context context) {
        return null;
    }
}
