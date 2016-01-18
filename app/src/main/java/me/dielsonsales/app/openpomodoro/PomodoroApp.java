package me.dielsonsales.app.openpomodoro;

import android.app.Application;
import android.app.Service;

import me.dielsonsales.app.openpomodoro.dagger2.AppModule;
import me.dielsonsales.app.openpomodoro.dagger2.ServiceComponent;
import me.dielsonsales.app.openpomodoro.dagger2.ServiceModule;

public class PomodoroApp extends Application {
    private static ServiceComponent mServiceComponent;
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this; // needs to be accessed from static methods
    }

    public static ServiceComponent getServiceComponent(Service service) {
        mServiceComponent = me.dielsonsales.app.openpomodoro.dagger2.DaggerServiceComponent.builder()
                .appModule(new AppModule(mApplication))
                .serviceModule(new ServiceModule(service))
                .build();
        return mServiceComponent;
    }
}
