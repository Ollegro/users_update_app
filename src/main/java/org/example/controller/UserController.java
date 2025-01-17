package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class); // Логгер

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Проверка имени
        if (user.getName() == null || user.getName().isEmpty()) {
            String errorMessage = "Ошибка: Поле 'name' обязательно";
            logger.error(errorMessage); // Логирование ошибки в консоль сервера
            return ResponseEntity.badRequest().body(errorMessage); // Возврат ошибки клиенту
        }

        // Проверка возраста
        if (user.getAge() < 16 || user.getAge() > 90) {
            String errorMessage = "Ошибка: Возраст должен быть от 16 до 90 лет. Введенный возраст: " + user.getAge();
            logger.error(errorMessage); // Логирование ошибки в консоль сервера
            return ResponseEntity.badRequest().body(errorMessage); // Возврат ошибки клиенту
        }

        // Проверка должности
        if (user.getPosition() == null || user.getPosition().isEmpty()) {
            String errorMessage = "Ошибка: Поле 'position' обязательно";
            logger.error(errorMessage); // Логирование ошибки в консоль сервера
            return ResponseEntity.badRequest().body(errorMessage); // Возврат ошибки клиенту
        }

        // Создание пользователя
        User createdUser = userService.createUser(user);
        logger.info("Пользователь создан: {}", createdUser); // Логирование успешного создания
        return ResponseEntity.ok(createdUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            logger.info("Пользователь найден: {}", user.get()); // Логирование успешного поиска
            return ResponseEntity.ok(user.get());
        } else {
            String errorMessage = "Ошибка: Пользователь с ID " + id + " не найден";
            logger.error(errorMessage); // Логирование ошибки
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editUserById(@PathVariable Long id, @RequestBody User user) {
        // Проверка возраста
        if (user.getAge() < 16 || user.getAge() > 90) {
            String errorMessage = "Ошибка: Возраст должен быть от 16 до 90 лет. Введенный возраст: " + user.getAge();
            logger.error(errorMessage); // Логирование ошибки
            return ResponseEntity.badRequest().body(errorMessage); // Возврат ошибки клиенту
        }

        Optional<User> existingUser = userService.getUserById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setAge(user.getAge());
            updatedUser.setPosition(user.getPosition());

            userService.updateUser(updatedUser);
            logger.info("Пользователь обновлен: {}", updatedUser); // Логирование успешного обновления
            return ResponseEntity.ok(updatedUser);
        } else {
            String errorMessage = "Ошибка: Пользователь с ID " + id + " не найден";
            logger.error(errorMessage); // Логирование ошибки
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        if (userService.existsById(id)) {
            userService.deleteUserById(id);
            logger.info("Пользователь с ID {} удален", id); // Логирование успешного удаления
            return ResponseEntity.noContent().build();
        } else {
            String errorMessage = "Ошибка: Пользователь с ID " + id + " не найден";
            logger.error(errorMessage); // Логирование ошибки
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        logger.info("Получен список всех пользователей: {}", users); // Логирование успешного получения списка
        return ResponseEntity.ok(users);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        logger.info("Все пользователи удалены"); // Логирование успешного удаления всех пользователей
        return ResponseEntity.noContent().build();
    }
}