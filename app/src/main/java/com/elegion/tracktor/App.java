package com.elegion.tracktor;

import android.app.Application;

import com.elegion.tracktor.di.AppModule;
import com.elegion.tracktor.di.RepositoryModule;

import io.realm.Realm;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.configuration.Configuration;
import toothpick.registries.FactoryRegistryLocator;
import toothpick.registries.MemberInjectorRegistryLocator;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {
    private static Scope sAppScope;


    @Override
    public void onCreate() {
        super.onCreate();

        sAppScope = Toothpick.openScope(App.class);
        sAppScope.installModules(new RepositoryModule(this));

        Realm.init(this);
    }
    public static Scope getAppScope() {
        return sAppScope;
    }
}
