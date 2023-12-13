package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    Task save(Task task);

    boolean update(Task task);

    boolean deleteById(int id);

    List<Task> findAll();

    Optional<Task> findById(int id);

    List<Task> findByDone();

    List<Task> findByNew();
}
