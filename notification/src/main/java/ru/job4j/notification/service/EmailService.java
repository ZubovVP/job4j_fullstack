package ru.job4j.notification.service;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.notification.model.Notification;
import ru.job4j.notification.mail.ActionsForEmail;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $.
 * Date: 01.06.2021.
 */
@Log4j2
@Service
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class.getName());
    private final ActionsForEmail mail;

    public EmailService(ActionsForEmail mail) {
        this.mail = mail;
    }

    public List<Notification> readMessages() {
        return this.mail.readAllMessages();
    }

    public boolean write(Notification notification) {
        String password = null;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(in);
            password = props.getProperty("password");
        } catch (Exception e) {
            LOGGER.error("Failed to download password.");
        }
        return this.mail.write(notification, password);
    }
}
