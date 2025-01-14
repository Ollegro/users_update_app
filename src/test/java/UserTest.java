import jakarta.persistence.*;
import org.example.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserEntityAnnotations() {

        assertTrue(User.class.isAnnotationPresent(Entity.class), "Класс User должен быть аннотирован как @Entity");
        assertTrue(User.class.isAnnotationPresent(Table.class), "Класс User должен быть аннотирован как @Table");

        Table tableAnnotation = User.class.getAnnotation(Table.class);
        assertEquals("users", tableAnnotation.name(), "Имя таблицы должно быть 'users'");
    }

    @Test
    public void testIdFieldAnnotations() throws NoSuchFieldException {

        assertTrue(User.class.getDeclaredField("id").isAnnotationPresent(Id.class), "Поле id должно быть аннотировано как @Id");
        assertTrue(User.class.getDeclaredField("id").isAnnotationPresent(GeneratedValue.class), "Поле id должно быть аннотировано как @GeneratedValue");

        GeneratedValue generatedValueAnnotation = User.class.getDeclaredField("id").getAnnotation(GeneratedValue.class);
        assertEquals(GenerationType.IDENTITY, generatedValueAnnotation.strategy(), "Стратегия генерации должна быть IDENTITY");
    }

    @Test
    public void testNameFieldAnnotations() throws NoSuchFieldException {

        assertTrue(User.class.getDeclaredField("name").isAnnotationPresent(Column.class), "Поле name должно быть аннотировано как @Column");

        Column columnAnnotation = User.class.getDeclaredField("name").getAnnotation(Column.class);
        assertFalse(columnAnnotation.nullable(), "Поле name не должно быть nullable");
    }

    @Test
    public void testAgeFieldAnnotations() throws NoSuchFieldException {

        assertTrue(User.class.getDeclaredField("age").isAnnotationPresent(Column.class), "Поле age должно быть аннотировано как @Column");

        Column columnAnnotation = User.class.getDeclaredField("age").getAnnotation(Column.class);
        assertFalse(columnAnnotation.nullable(), "Поле age не должно быть nullable");
    }

    @Test
    public void testPositionFieldAnnotations() throws NoSuchFieldException {

        assertTrue(User.class.getDeclaredField("position").isAnnotationPresent(Column.class), "Поле position должно быть аннотировано как @Column");

        Column columnAnnotation = User.class.getDeclaredField("position").getAnnotation(Column.class);
        assertFalse(columnAnnotation.nullable(), "Поле position не должно быть nullable");
    }

    @Test
    public void testGettersAndSetters() {

        User user = new User();


        user.setId(1L);
        user.setName("Gutsev Oleg");
        user.setAge(50);
        user.setPosition("Developer");


        assertEquals(1L, user.getId(), "ID должно быть 1");
        assertEquals("Gutsev Oleg", user.getName(), "Имя должно быть 'Gutsev Oleg'");
        assertEquals(50, user.getAge(), "Возраст должен быть 50");
        assertEquals("Developer", user.getPosition(), "Должность должна быть 'Developer'");
    }

    @Test
    public void testEqualsAndHashCode() {

        User user1 = new User();
        user1.setId(1L);
        user1.setName("Gutsev Oleg");
        user1.setAge(50);
        user1.setPosition("Developer");

        User user2 = new User();
        user2.setId(1L);
        user2.setName("Gutsev Oleg");
        user2.setAge(50);
        user2.setPosition("Developer");


        assertEquals(user1, user2, "Объекты User должны быть равны");
        assertEquals(user1.hashCode(), user2.hashCode(), "Хэш-коды объектов User должны быть равны");
    }

    @Test
    public void testToString() {

        User user = new User();
        user.setId(1L);
        user.setName("Gutsev Oleg");
        user.setAge(50);
        user.setPosition("Developer");


        String expectedToString = "User{id=1, name='Gutsev Oleg', age=50, position='Developer'}";
        assertEquals(expectedToString, user.toString(), "Метод toString должен возвращать корректное значение");
    }
}