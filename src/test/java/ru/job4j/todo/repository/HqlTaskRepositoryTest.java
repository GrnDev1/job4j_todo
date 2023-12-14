package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlTaskRepositoryTest {
    private static SessionFactory sf;
    private static HqlTaskRepository taskRepository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        taskRepository = new HqlTaskRepository(sf);
    }

    @AfterEach
    public void clearTasks() {
        var tasks = taskRepository.findAll();
        for (var task : tasks) {
            taskRepository.deleteById(task.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var task = new Task();
        task.setId(1);
        task.setTitle("Task1");
        var savedTask = taskRepository.save(task);
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        var task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task1");
        var task2 = new Task();
        task2.setId(2);
        task2.setDone(true);
        var savedTask1 = taskRepository.save(task1);
        var savedTask2 = taskRepository.save(task2);
        var result = taskRepository.findAll();
        assertThat(result).isEqualTo(List.of(savedTask1, savedTask2));
    }

    @Test
    public void whenSaveSeveralThenGetDone() {
        var task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task1");
        task1.setDone(true);
        var task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task2");
        task2.setDone(true);
        var savedTask1 = taskRepository.save(task1);
        var savedTask2 = taskRepository.save(task2);
        var result = taskRepository.findByDone();
        assertThat(result).isEqualTo(List.of(savedTask1, savedTask2));
    }

    @Test
    public void whenSaveSeveralThenGetNew() {
        var task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task1");
        var task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task2");
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
        task1.setId(1);
        task1.setTitle("Task1");
        var savedTask1 = taskRepository.save(task1);
        var isDeleted = taskRepository.deleteById(savedTask1.getId());
        var savedTask = taskRepository.findById(savedTask1.getId());
        assertThat(isDeleted).isTrue();
        assertThat(savedTask).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        var task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task1");
        var savedTask1 = taskRepository.save(task1);
        var task2 = new Task();
        task2.setId(savedTask1.getId());
        task2.setTitle("Task2");
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
        var isUpdated = taskRepository.update(task1);
        assertThat(isUpdated).isFalse();
    }
}