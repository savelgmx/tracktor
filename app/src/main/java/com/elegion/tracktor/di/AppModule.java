package com.elegion.tracktor.di;

import android.content.Context;

import com.elegion.tracktor.App;
import com.elegion.tracktor.ui.map.MainViewModel;

import toothpick.config.Module;

public class AppModule extends Module {
    private final App mApp;
    private Context mContext;

    public AppModule(App app) {
        this.mApp = app;

// https://medium.com/swlh/toothpick-3-a-hidden-gem-194ae6ee8671
        mContext = app.getApplicationContext();

        bind(App.class).toInstance(mApp);
        // нужно в AppModule определенно прописать
        bind(MainViewModel.class);
        bind(Context.class).toInstance(provideContext());
    }

    App provideApp() {
        return mApp;
    }

    Context provideContext(){

        return mContext;
    }

}
