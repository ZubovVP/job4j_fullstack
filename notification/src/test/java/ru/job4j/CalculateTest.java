package ru.job4j;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Intellij IDEA.
 * User: Vitaly Zubov.
 * Email: Zubov.VP@yandex.ru.
 * Version: $.
 * Date: 26.05.2021.
 */
public class CalculateTest {

    @Test
    public void addTest() {
        assertThat(new Calculate().add(1, 1), is(2));
    }
}