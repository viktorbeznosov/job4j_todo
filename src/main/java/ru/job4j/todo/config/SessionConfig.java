package ru.job4j.todo.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

@Service
public class SessionConfig {
    @Bean(destroyMethod = "close")
    public SessionFactory sf() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}
