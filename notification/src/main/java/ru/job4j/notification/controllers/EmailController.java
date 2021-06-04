package ru.job4j.notification.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.notification.model.Notification;
import ru.job4j.notification.service.EmailService;

import java.util.List;


/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 08.02.2021.
 */
@RestController
public class EmailController {
    private final EmailService es;

    public EmailController(EmailService es) {
        this.es = es;
    }

    @GetMapping("/read")
    public List<Notification> checkEmail(@RequestParam("email") String email, @RequestParam("password") String password) {
        return this.es.readMessages(email, password);
    }

    @PostMapping("/send")
    public ResponseEntity<Void> checkEmail(@RequestBody Notification notification) {
        boolean result = this.es.write(notification.getTo(), notification.getFrom(), notification.getSubject(), notification.getBody());
        return result ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();

    }
}
