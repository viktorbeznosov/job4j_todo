package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserStore userStore;

    public Optional<User> add(User user) {
        return userStore.add(user);
    }

    public Optional<User> findByLogin(String login) {
        return userStore.findByLogin(login);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        return userStore.findByLogin(login)
                .filter(user -> user.getPassword().equals(password));
    }
}