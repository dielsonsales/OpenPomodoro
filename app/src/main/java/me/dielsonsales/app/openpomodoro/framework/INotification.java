package me.dielsonsales.app.openpomodoro.framework;

/**
 * Created by dielson on 21/01/16.
 */
public interface INotification {
    enum NotificationType {
        WORK_NOTIFICATION,
        REST_NOTIFICATION
    }

    void showNotification(NotificationType notificationType);

    void hideNotification();
}
