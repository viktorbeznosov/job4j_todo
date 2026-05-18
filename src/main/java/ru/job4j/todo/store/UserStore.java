package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore {

    private final SessionFactory sf;

    public Optional<User> add(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            return Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
            return Optional.empty();
        } finally {
            session.close();
        }
    }

    public Optional<User> findByLogin(String login) {
        Session session = sf.openSession();
        Query<User> query = session.createQuery(
                "from User where login = :login", User.class);
        query.setParameter("login", login);
        Optional<User> result = query.uniqueResultOptional();
        session.close();
        return result;
    }
}