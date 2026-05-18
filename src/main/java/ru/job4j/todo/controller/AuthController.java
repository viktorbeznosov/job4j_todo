package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        model.addAttribute("error", error != null);
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String login, @RequestParam String password, HttpServletRequest request) {
        User user = userService.findByLoginAndPassword(login, password).orElse(null);
        if (user == null) {
            return "redirect:/login?error=true";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        return "redirect:/tasks";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.findByLogin(user.getLogin()).isPresent()) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "auth/register";
        }
        userService.add(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}