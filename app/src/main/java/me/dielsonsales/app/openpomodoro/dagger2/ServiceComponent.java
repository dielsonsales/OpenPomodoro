package me.dielsonsales.app.openpomodoro.dagger2;

import javax.inject.Singleton;

import dagger.Component;
import me.dielsonsales.app.openpomodoro.presentation.ServicePresenter;

@Singleton
@Component(modules = {AppModule.class, ServiceModule.class})
public interface ServiceComponent {
    void inject(ServicePresenter servicePresenter);
}
