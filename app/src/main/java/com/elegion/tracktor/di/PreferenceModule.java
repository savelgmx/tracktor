package com.elegion.tracktor.di;

import com.elegion.tracktor.App;
import com.elegion.tracktor.ui.preferences.MainPreferences;

import toothpick.config.Module;

public class PreferenceModule extends Module {
    private final App mApp;

    public PreferenceModule(App app) {
        this.mApp = app;
        bind(App.class).toInstance(provideApp());
        bind(MainPreferences.class).toInstance(providePreferences());
    }

    private MainPreferences providePreferences() {
        return new MainPreferences();
    }

    private App provideApp() {
        return mApp;
    }

}

