import org.example.controller.UserController;
import org.example.model.User;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    public void testCreateUser() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");

        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());

        verify(userService, times(1)).createUser(user);
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

        verify(userService, times(1)).existsById(userId);
        verify(userService, never()).deleteUserById(userId);
    }
}