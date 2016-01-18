package me.dielsonsales.app.openpomodoro.dagger2;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    Application mApp;

    public AppModule(Application app) { mApp = app; }

    @Provides
    @Singleton
    Application providesApplication() { return mApp; }
}
