package org.example.repository;

import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

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
}