package org.example.controller;

import org.example.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class UserWebController {

    private final WebClient webClient;

    public UserWebController(WebClient webClient) {
        this.webClient = webClient;
    }

    // Главная страница со списком пользователей
    @GetMapping("/")
    public String index(Model model) {
        // Вызываем REST-контроллер через WebClient
        List<User> users = webClient.get()
                .uri("/users") // Эндпоинт REST-контроллера
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block(); // Блокируем выполнение, чтобы получить результат

        model.addAttribute("users", users);
        return "index.html";
    }

    // Обработка добавления пользователя
    @PostMapping("/add-user")
    public String createUser(@ModelAttribute User user) {
        // Вызываем REST-контроллер через WebClient
        webClient.post()
                .uri("/users") // Эндпоинт REST-контроллера
                .bodyValue(user)
                .retrieve()
                .toBodilessEntity()
                .block(); // Блокируем выполнение

        return "redirect:/"; // Перенаправление на главную страницу
    }



    // Обработка редактирования пользователя
    @PostMapping("/edit/{id}")
    public String updateUserById(@PathVariable Long id, @RequestBody User user) {
        // Вызываем REST-контроллер через WebClient для обновления пользователя
        webClient.put()
                .uri("/users/{id}", id)
                .bodyValue(user)
                .retrieve()
                .toBodilessEntity()
                .block();

        return "redirect:/"; // Перенаправление на главную страницу
    }



    // Обработка удаления пользователя
    @GetMapping("/delete/{id}")
    public String deleteUserById(@PathVariable Long id) {
        // Вызываем REST-контроллер через WebClient
        webClient.delete()
                .uri("/users/{id}", id) // Эндпоинт REST-контроллера
                .retrieve()
                .toBodilessEntity()
                .block(); // Блокируем выполнение

        return "redirect:/"; // Перенаправление на главную страницу
    }
}