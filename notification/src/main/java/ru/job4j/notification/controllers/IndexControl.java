package ru.job4j.notification.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $Id$.
 * Date: 03.12.2020.
 */
@RestController
public class IndexControl {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
