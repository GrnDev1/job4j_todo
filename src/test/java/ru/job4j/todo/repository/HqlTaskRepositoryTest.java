package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlTaskRepositoryTest {
    private static TaskRepository taskRepository;
    private static CrudRepository crudRepository;
    private static PriorityRepository priorityRepository;
    static User user;
    static List<Category> categories;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        taskRepository = new HqlTaskRepository(crudRepository);
        UserRepository userRepository = new HqlUserRepository(crudRepository);
        priorityRepository = new HqlPriorityRepository(crudRepository);
        CategoryRepository categoryRepository = new HqlCategoryRepository(crudRepository);

        user = new User();
        user.setName("roman");
        user.setLogin("roman@mail.ru");
        user.setPassword("password");
        userRepository.save(user);
        categories = categoryRepository.findAllById(List.of(1, 2, 3));
    }

    @AfterEach
    public void clearTasks() {
        var tasks = taskRepository.findAll();
        for (var task : tasks) {
            taskRepository.deleteById(task.getId());
        }

    }

    @AfterAll
    public static void clearUser() {
        crudRepository.run("DELETE FROM User", Map.of());
    }

    @Test
    public void whenSaveThenGetSame() {
        var task = new Task();
        task.setTitle("Task1");
        task.setUser(user);
        var savedTask = taskRepository.save(task);
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var task1 = new Task();
        task1.setTitle("Task1");
        task1.setUser(user);
        var task2 = new Task();
        task2.setDone(true);
        task2.setUser(user);
        var savedTask1 = taskRepository.save(task1);
        var savedTask2 = taskRepository.save(task2);
        var result = taskRepository.findAll();
        assertThat(result).isEqualTo(List.of(savedTask1, savedTask2));
    }

    @Test
    public void whenSaveSeveralThenGetDone() {
        var task1 = new Task();
        task1.setTitle("Task1");
        task1.setDone(true);
        task1.setUser(user);
        var task2 = new Task();
        task2.setTitle("Task2");
        task2.setDone(true);
        task2.setUser(user);
        var savedTask1 = taskRepository.save(task1);
        var savedTask2 = taskRepository.save(task2);
        var result = taskRepository.findByDone();
        assertThat(result).isEqualTo(List.of(savedTask1, savedTask2));
    }

    @Test
    public void whenSaveSeveralThenGetNew() {
        var task1 = new Task();
        task1.setTitle("Task1");
        task1.setUser(user);
        var task2 = new Task();
        task2.setTitle("Task2");
        task2.setUser(user);
        var savedTask1 = taskRepository.save(task1);
        var savedTask2 = taskRepository.save(task2);
        var result = taskRepository.findByNew();
        assertThat(result).isEqualTo(List.of(savedTask1, savedTask2));
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(taskRepository.findAll()).isEqualTo(emptyList());
        assertThat(taskRepository.findById(0)).isEqualTo(empty());
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        var task1 = new Task();
        task1.setTitle("Task1");
        task1.setUser(user);
        var savedTask1 = taskRepository.save(task1);
        var isDeleted = taskRepository.deleteById(savedTask1.getId());
        var savedTask = taskRepository.findById(savedTask1.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTask).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var task1 = new Task();
        task1.setTitle("Task1");
        task1.setUser(user);
        var savedTask1 = taskRepository.save(task1);
        var task2 = new Task();
        task2.setId(savedTask1.getId());
        task2.setTitle("Task2");
        task2.setUser(user);
        task2.setPriority(priorityRepository.findAll().get(0));
        task2.setCategories(categories);
        var isUpdated = taskRepository.update(task2);
        var updatedTask = taskRepository.findById(task2.getId()).get();
        assertThat(isUpdated).isTrue();
        assertThat(updatedTask).usingRecursiveComparison().isEqualTo(task2);
    }

    @Test
    public void whenUpdateUnExistingTaskThenGetFalse() {
        var task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task1");
        task1.setUser(user);
        task1.setPriority(priorityRepository.findAll().get(0));
        task1.setCategories(categories);
        var isUpdated = taskRepository.update(task1);
        assertThat(isUpdated).isFalse();
    }
}