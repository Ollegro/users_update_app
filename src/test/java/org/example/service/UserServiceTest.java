package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getAge(), createdUser.getAge());
        assertEquals(user.getPosition(), createdUser.getPosition());

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserById_UserFound() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testGetUserById_UserNotFound() {

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(userId);

        assertFalse(foundUser.isPresent());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void testDeleteUserById() {

        Long userId = 1L;

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testExistsById_UserExists() {

        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        boolean exists = userService.existsById(userId);

        assertTrue(exists);

        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    public void testExistsById_UserNotExists() {

        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        boolean exists = userService.existsById(userId);

        assertFalse(exists);

        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    public void testDeleteAllUsers() {
        // Мокируем вызов userRepository.deleteAll()
        doNothing().when(userRepository).deleteAll();

        userService.deleteAllUsers();

        verify(userRepository, times(1)).deleteAll();
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

        // Мокируем вызов userRepository.findAll()
        when(userRepository.findAll()).thenReturn(new ArrayList<>(users));

        Set<User> foundUsers = userService.getAllUsers();

        assertEquals(users, foundUsers);

        verify(userRepository, times(1)).findAll();
    }
}