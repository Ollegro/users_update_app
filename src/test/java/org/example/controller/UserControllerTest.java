package org.example.controller;

import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testCreateUser_Success() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");

        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService, times(1)).createUser(user);
    }

    @Test
    public void testCreateUser_NameIsEmpty() {
        User user = new User();
        user.setAge(30);
        user.setPosition("Developer");

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Поле 'name' обязательно", response.getBody());

        verify(userService, never()).createUser(user);
    }

    @Test
    public void testCreateUser_AgeIsNotPositive() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(-5);
        user.setPosition("Developer");

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Поле 'age' должно быть положительным числом", response.getBody());

        verify(userService, never()).createUser(user);
    }

    @Test
    public void testCreateUser_PositionIsEmpty() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Поле 'position' обязательно", response.getBody());

        verify(userService, never()).createUser(user);
    }

    @Test
    public void testGetUserById_UserFound() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetUserById_UserNotFound() {

        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testDeleteUserById_UserExists() {

        Long userId = 1L;

        when(userService.existsById(userId)).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUserById(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userService, times(1)).existsById(userId);
        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    public void testDeleteUserById_UserNotExists() {
        Long userId = 1L;

        when(userService.existsById(userId)).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUserById(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Проверка, что метод userService.existsById был вызван, а userService.deleteUserById — нет
        verify(userService, times(1)).existsById(userId);
        verify(userService, never()).deleteUserById(userId);
    }

    @Test
    public void testDeleteAllUsers() {

        doNothing().when(userService).deleteAllUsers();

        ResponseEntity<Void> response = userController.deleteAllUsers();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userService, times(1)).deleteAllUsers();
    }

    @Test
    public void testGetAllUsers() {

        User user1 = new User();
        user1.setName("Ivan Ivanov");
        user1.setAge(30);
        user1.setPosition("Developer");

        User user2 = new User();
        user2.setName("Petr Petrov");
        user2.setAge(25);
        user2.setPosition("Designer");

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<Set<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());

        // Проверка, что метод userService.getAllUsers был вызван 1 раз
        verify(userService, times(1)).getAllUsers();
    }
}