package com.elegion.tracktor.di;


import android.content.Context;
import com.elegion.tracktor.App;
import toothpick.config.Module;


public class ContextModule extends Module {
    private final App myApp;

    public ContextModule(App app) {
        myApp = app;
        bind(App.class).toInstance(provideApp());
        bind(Context.class).toInstance(provideContext());
    }

    private App provideApp() {
        return myApp;
    }


    public Context provideContext() {
            return myApp.getApplicationContext();
        }
    }

