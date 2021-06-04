package ru.job4j.notification.mail;

import ru.job4j.notification.model.Notification;

import java.util.List;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $.
 * Date: 04.06.2021.
 */
public interface ActionsForEmail {
    List<Notification> readAllMessages(String email, String password);

    boolean write(Notification email, String password);
}
