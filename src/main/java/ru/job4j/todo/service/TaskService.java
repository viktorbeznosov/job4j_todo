package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.request.tasks.GetAllTasksRequest;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskStore taskStore;

    public List<Task> findByFilter(GetAllTasksRequest request) {
        return taskStore.findByFilter(request);
    }

    public Optional<Task> findById(int id) {
        return taskStore.findById(id);
    }

    public Task save(String title, String description) {
        Task task = new Task(title, description, LocalDateTime.now(), false);
        return taskStore.add(task);
    }

    public boolean update(Task task) {
        return taskStore.update(task);
    }

    public boolean setDoneById(int id) {
        return taskStore.setDoneById(id);
    }

    public boolean delete(int id) {
        return taskStore.delete(id);
    }
}