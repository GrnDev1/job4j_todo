package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.mappers.TaskMapper;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskDtoService implements TaskDtoService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Override
    public Optional<TaskDto> findById(int id) {
        var taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return Optional.empty();
        }
        var task = taskOptional.get();
        return Optional.of(taskMapper.getTaskFromEntity(task));
    }

    @Override
    public List<TaskDto> findAll() {
        return taskMapper.getListFromEntity(taskRepository.findAll());
    }

    @Override
    public List<TaskDto> findByDone() {
        return taskMapper.getListFromEntity(taskRepository.findByDone());
    }

    @Override
    public List<TaskDto> findByNew() {
        return taskMapper.getListFromEntity(taskRepository.findByNew());
    }
}