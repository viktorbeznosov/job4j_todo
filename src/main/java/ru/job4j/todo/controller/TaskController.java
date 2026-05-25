package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.todo.dto.request.tasks.GetAllTasksRequest;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    static private final String TASK_NOT_FOUND = "Задача не найдена";

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping
    public String getAllTasks(
        @ModelAttribute GetAllTasksRequest request,
        @RequestParam(required = false) Boolean done,
        Model model
    ) {
        model.addAttribute("tasks", taskService.findByFilter(request));
        model.addAttribute("filter", request);

        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getTask(
            @PathVariable int id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", TASK_NOT_FOUND);
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        return "tasks/detail";
    }

    @GetMapping("/create")
    public String createForm() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String description,
            @RequestParam String title
    ) {
        taskService.save(title, description);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/done")
    public String markDone(
            @PathVariable int id,
            Model model
    ) {
        if (!taskService.setDoneById(id)) {
            model.addAttribute("message", "Ошибка редактирования задачи");
            return "errors/409";
        }
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable int id,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Task> task = taskService.findById(id);
        if (task.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", TASK_NOT_FOUND);
            return "redirect:/tasks";
        }
        model.addAttribute("task", task.get());
        return "tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String description,
            Model model
    ) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);

        if (!taskService.update(task)) {
            model.addAttribute("message", "Ошибка редактирования задачи");
            return "errors/409";
        }
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(
            @PathVariable int id,
            Model model
    ) {
        if (!taskService.delete(id)) {
            model.addAttribute("message", "Задача не найдена");
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}