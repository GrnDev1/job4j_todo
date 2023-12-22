package ru.job4j.todo.service;

import ru.job4j.todo.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskDtoService {
    List<TaskDto> findAll();

    Optional<TaskDto> findById(int id);

    List<TaskDto> findByDone();

    List<TaskDto> findByNew();
}
