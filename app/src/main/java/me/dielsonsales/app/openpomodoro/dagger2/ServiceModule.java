package me.dielsonsales.app.openpomodoro.dagger2;

import android.app.Application;
import android.app.Service;
import android.os.PowerManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.dielsonsales.app.openpomodoro.android.INotification;
import me.dielsonsales.app.openpomodoro.android.ISoundPlayer;
import me.dielsonsales.app.openpomodoro.android.IVibrator;
import me.dielsonsales.app.openpomodoro.android.PomodoroNotificationManager;
import me.dielsonsales.app.openpomodoro.android.PomodoroSoundManager;
import me.dielsonsales.app.openpomodoro.android.PomodoroVibrator;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

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
    @Singleton
    PowerManager.WakeLock providesWakeLock(Application application) {
        return ((PowerManager) application.getSystemService(POWER_SERVICE))
                .newWakeLock(PARTIAL_WAKE_LOCK, "ServiceWakeLock");
    }
}
