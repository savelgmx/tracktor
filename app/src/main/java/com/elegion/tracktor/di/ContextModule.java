package com.elegion.tracktor.di;

import android.app.Application;
import android.content.Context;

import com.elegion.tracktor.App;

import javax.inject.Named;
import javax.inject.Singleton;

import toothpick.config.Module;

/*
https://stackoverflow.com/questions/51451819/how-to-get-context-in-android-mvvm-viewmodel


0

I created it this way:

@Module
public class ContextModule {

    @Singleton
    @Provides
    @Named("AppContext")
    public Context provideContext(Application application) {
        return application.getApplicationContext();
    }
}
And then I just added in AppComponent the ContextModule.class:

@Component(
       modules = {
                ...
               ContextModule.class
       }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {
.....
}
And then I injected the context in my ViewModel:

@Inject
@Named("AppContext")
Context context;
 */

public class ContextModule extends Module {

    private final Context mContext;

    public ContextModule(Context mContext) {
        this.mContext = mContext;
        bind(Context.class).toInstance(mContext);
    }

}