package ru.practicum.stat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс микросервиса статистики ExploreWithMeStat
 *
 * @since 1.0
 */
@SpringBootApplication
public class ExploreWithMeStat {

    /**
     * Главный метод микросервиса, отвечающий за его запуск
     *
     * @param args передаваемые аргументы
     * @since 1.0
     */
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeStat.class, args);
    }
}
