package com.elegion.tracktor;

import android.app.Application;

import com.elegion.tracktor.di.AppModule;
import com.elegion.tracktor.di.NetworkModule;

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

        Realm.init(this);
        //==========Tooth Prick==========================
        Toothpick.setConfiguration(Configuration.forProduction().disableReflection());
        MemberInjectorRegistryLocator.setRootRegistry(new com.elegion.tracktor.MemberInjectorRegistry());
        FactoryRegistryLocator.setRootRegistry(new com.elegion.tracktor.FactoryRegistry());

        sAppScope = Toothpick.openScope(App.class);
        sAppScope.installModules(new SmoothieApplicationModule(this), new NetworkModule(), new AppModule(this));
        //===================================================


    }
}
