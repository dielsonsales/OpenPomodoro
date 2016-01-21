package me.dielsonsales.app.openpomodoro.dagger2;

import android.app.Application;
import android.app.Service;
import android.os.PowerManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.dielsonsales.app.openpomodoro.android.INotification;
import me.dielsonsales.app.openpomodoro.android.PomodoroNotificationManager;
import me.dielsonsales.app.openpomodoro.android.PomodoroSoundManager;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

@Module
public class ServiceModule {

    private static final String WAKE_LOCK_TAG = "ServiceWakeLock";
    private Service mService;

    public ServiceModule(Service service) { mService = service; }

    @Provides
    Service providesService() {
        return mService;
    }

    @Provides
    @Singleton
    PomodoroSoundManager providesSoundManager(Application application) {
        return new PomodoroSoundManager(application);
    }

    @Provides
    @Singleton
    INotification providesNotificationManager(Service service) {
        return new PomodoroNotificationManager(service);
    }

    @Provides
    @Singleton
    PowerManager.WakeLock providesWakeLock(Application application) {
        return ((PowerManager) application.getSystemService(POWER_SERVICE))
                .newWakeLock(PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    }
}
