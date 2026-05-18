package ru.job4j.todo;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.job4j.todo.model.Task;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean(destroyMethod = "close")
    public SessionFactory sf() {
        return new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Task.class)
            .buildSessionFactory();
    }
}