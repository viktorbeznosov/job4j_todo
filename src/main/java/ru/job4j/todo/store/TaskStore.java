package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.dto.request.tasks.GetAllTasksRequest;
import ru.job4j.todo.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {

    private final CrudStore store;

    public List<Task> findByFilter(GetAllTasksRequest request) {
        Map<String, Object> arguments = new HashMap<>();
        StringBuilder hql = new StringBuilder("FROM Task WHERE 1=1");
        if (request.getDone() != null) {
            hql.append(" AND done = :done");
            arguments.put("done", request.getDone());
        }
        hql.append(" ORDER BY created DESC");

        return store.query(hql.toString(), Task.class, arguments);
    }

    public boolean setDoneById(int id) {
        try {
            store.run("UPDATE Task t SET t.done = true WHERE t.id = :id",
                    Map.of("id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<Task> findById(int id) {
        return store.optional(
                "FROM Task WHERE id = :id", Task.class,
                Map.of("id", id)
        );
    }

    public Task add(Task task) {
        try {
            store.run(session -> session.persist(task));
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean update(Task task) {
        try {
            store.run(session -> session.merge(task));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try {
            store.run("DELETE Task t WHERE t.id = :id", Map.of("id", id));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}