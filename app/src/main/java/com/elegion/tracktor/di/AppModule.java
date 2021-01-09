package com.elegion.tracktor.di;

import com.elegion.tracktor.App;
import com.elegion.tracktor.ui.map.MainViewModel;

import toothpick.config.Module;

public class AppModule extends Module {
    private final App mApp;

    public AppModule(App app) {
        this.mApp = app;
        bind(App.class).toInstance(mApp);
        // нужно в AppModule определенно прописать
        bind(MainViewModel.class);
    }

    App provideApp() {
        return mApp;
    }

}
