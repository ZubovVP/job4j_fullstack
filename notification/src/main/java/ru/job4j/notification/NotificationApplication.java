package ru.job4j.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.job4j.notification.mail.ActionsForEmail;
import ru.job4j.notification.mail.ReaderWriterMail;

import java.io.InputStream;
import java.util.Properties;


@Configuration
@EnableAutoConfiguration
@ComponentScan
public class NotificationApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    public ActionsForEmail createRearWriterEmailForGmail() {
        String host = null;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties props = new Properties();
            props.load(in);
            host = props.getProperty("host.gmail");
        } catch (Exception e) {
            LOGGER.error("Failed to download host from resources.");
        }
        return new ReaderWriterMail(host);
    }
}
