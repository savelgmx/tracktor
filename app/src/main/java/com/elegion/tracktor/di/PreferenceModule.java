package com.elegion.tracktor.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.elegion.tracktor.ui.preferences.ReadUserPreferences;
import com.elegion.tracktor.ui.preferences.UserRepository;

import toothpick.config.Module;



public class PreferenceModule extends Module {

    public PreferenceModule(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        bind(SharedPreferences.class).toInstance(sharedPreferences);
        bind(UserRepository.class)
                .to(ReadUserPreferences.class);

    }


}

