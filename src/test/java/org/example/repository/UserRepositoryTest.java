package org.example.repository;

import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindById_thenReturnUser() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");
        entityManager.persistAndFlush(user); // Сохраняем пользователя в тестовой БД

        Optional<User> foundUser = userRepository.findById(user.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(user.getName());
        assertThat(foundUser.get().getAge()).isEqualTo(user.getAge());
        assertThat(foundUser.get().getPosition()).isEqualTo(user.getPosition());
    }

    @Test
    public void whenInvalidId_thenReturnEmpty() {

        Long invalidId = -1L;

        Optional<User> foundUser = userRepository.findById(invalidId);

        assertThat(foundUser).isNotPresent();
    }

    @Test
    public void whenSaveUser_thenUserIsPersisted() {
        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(25);
        user.setPosition("Designer");

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getAge()).isEqualTo(user.getAge());
        assertThat(savedUser.getPosition()).isEqualTo(user.getPosition());
    }

    @Test
    public void whenDeleteUser_thenUserIsRemoved() {
        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");
        entityManager.persistAndFlush(user);

        userRepository.deleteById(user.getId());
        Optional<User> deletedUser = userRepository.findById(user.getId());

        assertThat(deletedUser).isNotPresent();
    }

    @Test
    public void whenExistsById_thenReturnTrue() {

        User user = new User();
        user.setName("Ivan Ivanov");
        user.setAge(30);
        user.setPosition("Developer");
        entityManager.persistAndFlush(user);

        boolean exists = userRepository.existsById(user.getId());

        assertThat(exists).isTrue();
    }

    @Test
    public void whenExistsById_thenReturnFalse() {

        Long invalidId = -1L;

        boolean exists = userRepository.existsById(invalidId);

        assertThat(exists).isFalse();
    }

    @Test
    public void testDeleteAllUsers() {

        User user1 = new User();
        user1.setName("Ivan Ivanov");
        user1.setAge(30);
        user1.setPosition("Developer");

        User user2 = new User();
        user2.setName("Petr Petrov");
        user2.setAge(25);
        user2.setPosition("Designer");

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);

        userRepository.deleteAll();

        Set<User> users = new HashSet<>(userRepository.findAll());
        assertThat(users).isEmpty();
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

        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);


        Set<User> users = new HashSet<>(userRepository.findAll());

        assertThat(users).hasSize(2);
        assertThat(users).contains(user1, user2);
    }
}