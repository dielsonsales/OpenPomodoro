package me.dielsonsales.app.openpomodoro.dagger2;

import android.app.Application;
import android.app.Service;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroController;
import me.dielsonsales.app.openpomodoro.data.IPreferences;
import me.dielsonsales.app.openpomodoro.data.PomodoroPreferences;
import me.dielsonsales.app.openpomodoro.framework.INotification;
import me.dielsonsales.app.openpomodoro.framework.ISoundPlayer;
import me.dielsonsales.app.openpomodoro.framework.IVibrator;
import me.dielsonsales.app.openpomodoro.framework.IWakeLock;
import me.dielsonsales.app.openpomodoro.framework.PomodoroNotificationManager;
import me.dielsonsales.app.openpomodoro.framework.PomodoroSoundManager;
import me.dielsonsales.app.openpomodoro.framework.PomodoroVibrator;
import me.dielsonsales.app.openpomodoro.framework.PomodoroWakeLock;

@Module
public class ServiceModule {
    private Service mService;
    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    Service providesService() {
        return mService;
    }

    @Provides
    @Singleton
    ISoundPlayer providesSoundPlayer(Application application) {
        return new PomodoroSoundManager(application);
    }

    @Provides
    @Singleton
    IVibrator providesVibrator(Application application) {
        return new PomodoroVibrator(application);
    }

    @Provides
    @Singleton
    INotification providesINotification(Service service) {
        return new PomodoroNotificationManager(service);
    }

    @Provides
    IPreferences providesPreferences(Application application) {
        return new PomodoroPreferences(application);
    }

    @Provides
    @Singleton
    IWakeLock providesWakeLock(Application application) {
        return new PomodoroWakeLock(application);
    }

    @Provides
    @Singleton
    PomodoroController providesPomodoroController(ISoundPlayer soundPlayer, IVibrator vibrator, INotification notification) {
        return new PomodoroController(soundPlayer, vibrator, notification);
    }
}
