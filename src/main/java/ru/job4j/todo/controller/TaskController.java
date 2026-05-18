package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping
    public String getAllTasks(@RequestParam(required = false) Boolean done, Model model, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        if (done == null) {
            model.addAttribute("tasks", taskService.findAll());
            model.addAttribute("filter", "all");
        } else {
            model.addAttribute("tasks", taskService.findByDone(done));
            model.addAttribute("filter", done ? "done" : "new");
        }
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getTask(@PathVariable int id, Model model, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Task task = taskService.findById(id);
        if (task == null) {
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        return "tasks/detail";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session) {
        if (getUser(session) == null) {
            return "redirect:/login";
        }
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam String description, HttpSession session) {
        if (getUser(session) == null) {
            return "redirect:/login";
        }
        taskService.save(description);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/done")
    public String markDone(@PathVariable int id, HttpSession session) {
        if (getUser(session) == null) {
            return "redirect:/login";
        }
        taskService.updateStatus(id, true);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model, HttpSession session) {
        User user = getUser(session);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Task task = taskService.findById(id);
        if (task == null) {
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable int id, @RequestParam String description, HttpSession session) {
        if (getUser(session) == null) {
            return "redirect:/login";
        }
        Task task = taskService.findById(id);
        if (task != null) {
            task.setDescription(description);
            taskService.update(task);
        }
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session) {
        if (getUser(session) == null) {
            return "redirect:/login";
        }
        taskService.delete(id);
        return "redirect:/tasks";
    }
}