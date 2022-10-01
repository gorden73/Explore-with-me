package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс микросервиса ExploreWithMeMain
 *
 * @since 1.0
 */
@SpringBootApplication
public class ExploreWithMeMain {

    /**
     * Главный метод микросервиса, отвечающий за его запуск
     *
     * @param args передаваемые аргументы
     * @since 1.0
     */
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeMain.class, args);
    }
}
