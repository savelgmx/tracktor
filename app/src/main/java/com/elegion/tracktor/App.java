package com.elegion.tracktor;

import android.app.Application;

import io.realm.Realm;
import toothpick.Scope;
import toothpick.Toothpick;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        //tootpick
        Scope appScope = Toothpick.openScope(App.class);


    }
}
