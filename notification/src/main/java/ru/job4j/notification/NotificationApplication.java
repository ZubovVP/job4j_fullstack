package ru.job4j.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import ru.job4j.notification.mail.ActionsForEmail;
import ru.job4j.notification.mail.ReaderWriterMail;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    public ActionsForEmail createRearWriterEmailForGmail(final JavaMailSender sender, @Value("${spring.mail.pop3.host}") String host,
                                                         @Value("${spring.mail.username}") String username,
                                                         @Value("${spring.mail.password}") String pwd) {
        return new ReaderWriterMail(sender, host, username, pwd);
    }
}
