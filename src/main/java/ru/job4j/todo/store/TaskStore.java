package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;

@Repository
@AllArgsConstructor
public class TaskStore {

    private final SessionFactory sf;

    public List<Task> findAll() {
        Session session = sf.openSession();
        Query<Task> query = session.createQuery("from Task order by created desc", Task.class);
        List<Task> tasks = query.getResultList();
        session.close();
        return tasks;
    }

    public List<Task> findByDone(boolean done) {
        Session session = sf.openSession();
        Query<Task> query = session.createQuery(
                "from Task where done = :done order by created desc", Task.class);
        query.setParameter("done", done);
        List<Task> tasks = query.getResultList();
        session.close();
        return tasks;
    }

    public Task findById(int id) {
        Session session = sf.openSession();
        Task task = session.get(Task.class, id);
        session.close();
        return task;
    }

    public Task add(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();
        return task;
    }

    public boolean update(Task task) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(task);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean delete(int id) {
        Session session = sf.openSession();
        Task task = session.get(Task.class, id);
        if (task != null) {
            session.beginTransaction();
            session.delete(task);
            session.getTransaction().commit();
            session.close();
            return true;
        }
        session.close();
        return false;
    }
}