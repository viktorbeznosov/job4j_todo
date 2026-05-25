package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore {

    private final CrudStore store;

    public Optional<User> add(User user) {
        try {
            store.run(session -> session.save(user));
            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> findByLogin(String login) {
        return store.optional(
            "FROM User WHERE login = :login",
            User.class,
            Map.of("login", login)
        );
    }
}