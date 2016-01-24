package me.dielsonsales.app.openpomodoro.dagger2;

import android.app.Application;
import android.app.Service;
import android.os.PowerManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.dielsonsales.app.openpomodoro.framework.INotification;
import me.dielsonsales.app.openpomodoro.framework.ISoundPlayer;
import me.dielsonsales.app.openpomodoro.framework.IVibrator;
import me.dielsonsales.app.openpomodoro.framework.PomodoroNotificationManager;
import me.dielsonsales.app.openpomodoro.framework.PomodoroSoundManager;
import me.dielsonsales.app.openpomodoro.framework.PomodoroVibrator;
import me.dielsonsales.app.openpomodoro.data.IPreferences;
import me.dielsonsales.app.openpomodoro.data.PomodoroPreferences;

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
    IPreferences providesPreferences(Application application) {
        return new PomodoroPreferences(application);
    }

    @Provides
    @Singleton
    PowerManager.WakeLock providesWakeLock(Application application) {
        return ((PowerManager) application.getSystemService(POWER_SERVICE))
                .newWakeLock(PARTIAL_WAKE_LOCK, "ServiceWakeLock");
    }
}
