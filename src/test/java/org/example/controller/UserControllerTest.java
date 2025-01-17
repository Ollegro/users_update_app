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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        assertEquals("Ошибка: Поле 'name' обязательно", response.getBody());

        verify(userService, never()).createUser(user);
    }

    @Test
    public void testCreateUser_AgeIsBelowMinimum() {
        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(15); // Возраст меньше 16
        user.setPosition("Developer");

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ошибка: Возраст должен быть от 16 до 90 лет. Введенный возраст: 15", response.getBody());

        verify(userService, never()).createUser(user);
    }

    @Test
    public void testCreateUser_AgeIsAboveMaximum() {
        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(91); // Возраст больше 90
        user.setPosition("Developer");

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ошибка: Возраст должен быть от 16 до 90 лет. Введенный возраст: 91", response.getBody());

        verify(userService, never()).createUser(user);
    }

    @Test
    public void testCreateUser_PositionIsEmpty() {
        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);

        ResponseEntity<?> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ошибка: Поле 'position' обязательно", response.getBody());

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
    public void testEditUserById_Success() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("Ivan Ivanov");
        updatedUser.setAge(30);
        updatedUser.setPosition("Senior Developer");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Ivan Ivanov");
        existingUser.setAge(25);
        existingUser.setPosition("Developer");

        when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(existingUser)).thenReturn(existingUser);

        ResponseEntity<?> response = userController.editUserById(userId, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(existingUser, response.getBody());

        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(existingUser);
    }

    @Test
    public void testEditUserById_AgeIsBelowMinimum() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("Ivan Ivanov");
        updatedUser.setAge(15); // Возраст меньше 16
        updatedUser.setPosition("Senior Developer");

        ResponseEntity<?> response = userController.editUserById(userId, updatedUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ошибка: Возраст должен быть от 16 до 90 лет. Введенный возраст: 15", response.getBody());

        verify(userService, never()).getUserById(userId);
        verify(userService, never()).updateUser(any());
    }

    @Test
    public void testEditUserById_AgeIsAboveMaximum() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("Ivan Ivanov");
        updatedUser.setAge(91); // Возраст больше 90
        updatedUser.setPosition("Senior Developer");

        ResponseEntity<?> response = userController.editUserById(userId, updatedUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ошибка: Возраст должен быть от 16 до 90 лет. Введенный возраст: 91", response.getBody());

        verify(userService, never()).getUserById(userId);
        verify(userService, never()).updateUser(any());
    }

    @Test
    public void testEditUserById_UserNotFound() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setName("Ivan Ivanov");
        updatedUser.setAge(30);
        updatedUser.setPosition("Senior Developer");

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.editUserById(userId, updatedUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(userService, times(1)).getUserById(userId);
        verify(userService, never()).updateUser(any());
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

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());

        verify(userService, times(1)).getAllUsers();
    }
}