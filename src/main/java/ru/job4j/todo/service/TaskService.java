package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskStore taskStore;

    public List<Task> findAll() {
        return taskStore.findAll();
    }

    public List<Task> findByDone(boolean done) {
        return taskStore.findByDone(done);
    }

    public Task findById(int id) {
        return taskStore.findById(id);
    }

    public Task save(String description) {
        Task task = new Task(description, LocalDateTime.now(), false);
        return taskStore.add(task);
    }

    public boolean update(Task task) {
        return taskStore.update(task);
    }

    public boolean updateStatus(int id, boolean done) {
        Task task = taskStore.findById(id);
        if (task != null) {
            task.setDone(done);
            return taskStore.update(task);
        }
        return false;
    }

    public boolean delete(int id) {
        return taskStore.delete(id);
    }
}