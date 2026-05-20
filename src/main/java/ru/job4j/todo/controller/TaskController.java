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
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    static private final String LOGIN_MESSAGE = "С начала авторизуйтесь на сайте";
    static private final String TASK_NOT_FOUND = "Задача не найдена";

    private User getUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping
    public String getAllTasks(
        @ModelAttribute GetAllTasksRequest request,
        @RequestParam(required = false) Boolean done,
        Model model,
        HttpSession session,
        RedirectAttributes redirectAttributes
    ) {
        User user = getUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        model.addAttribute("user", user);
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
        User user = getUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Task task = taskService.findById(id);
        if (task == null) {
            redirectAttributes.addFlashAttribute("message", TASK_NOT_FOUND);
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        return "tasks/detail";
    }

    @GetMapping("/create")
    public String createForm(HttpSession session, RedirectAttributes redirectAttributes) {
        if (getUser(session) == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String description,
            @RequestParam String title,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (getUser(session) == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        taskService.save(title, description);
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/done")
    public String markDone(
            @PathVariable int id,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (getUser(session) == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        taskService.setDoneById(id);
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable int id,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = getUser(session);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Task task = taskService.findById(id);
        if (task == null) {
            redirectAttributes.addFlashAttribute("message", TASK_NOT_FOUND);
            return "redirect:/tasks";
        }
        model.addAttribute("task", task);
        return "tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(
            @PathVariable int id,
            @RequestParam String title,
            @RequestParam String description,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (getUser(session) == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        Task task = taskService.findById(id);
        if (task != null) {
            task.setTitle(title);
            task.setDescription(description);
            taskService.update(task);
        }
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (getUser(session) == null) {
            redirectAttributes.addFlashAttribute("message", LOGIN_MESSAGE);
            return "redirect:/login";
        }
        taskService.delete(id);
        return "redirect:/tasks";
    }
}