package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.dto.request.tasks.GetAllTasksRequest;
import ru.job4j.todo.model.Task;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@AllArgsConstructor
public class TaskStore {

    private final SessionFactory sf;

    public List<Task> findByFilter(GetAllTasksRequest request) {
        Session session = sf.openSession();
        try {
            StringBuilder hql = new StringBuilder("FROM Task WHERE 1=1");
            if (request.getDone() != null) {
                hql.append(" AND done = :done");
            }

            hql.append(" ORDER BY created DESC");

            Query<Task> query = session.createQuery(hql.toString(), Task.class);

            if (request.getDone() != null) {
                query.setParameter("done", request.getDone());
            }

            List<Task> tasks = query.getResultList();
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            session.close();
        }

    }

    public boolean setDoneById(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("UPDATE Task t SET t.done = true WHERE t.id = :id");
            query.setParameter("id", id);
            int updatedRows = query.executeUpdate();
            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    public Task findById(int id) {
        Session session = sf.openSession();
        try {
            Task task = session.get(Task.class, id);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public Task add(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }

    }

    @Transactional
    public boolean update(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("""
            UPDATE Task t 
            SET t.description = :description,
            t.title = :title
            WHERE t.id = :id
            """);
            query.setParameter("id", task.getId());
            query.setParameter("description", task.getDescription());
            query.setParameter("title", task.getTitle());

            int updatedRows = query.executeUpdate();
            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }

    }

    @Transactional
    public boolean delete(int id) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<Task> query = session.createQuery("""
            DELETE Task t 
            WHERE t.id = :id
            """);
            query.setParameter("id", id);

            int updatedRows = query.executeUpdate();
            session.getTransaction().commit();
            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }
}