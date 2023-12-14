package ru.job4j.todo.service;

import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTaskServiceTest {
    private final TaskRepository taskRepository = mock(TaskRepository.class);
    private final TaskService taskService = new SimpleTaskService(taskRepository);

    @Test
    public void whenFindAllThenGetSameList() {
        var task = new Task();
        task.setId(1);
        List<Task> list = List.of(task);
        when(taskRepository.findAll()).thenReturn(list);
        var savedList = taskService.findAll();
        assertThat(savedList).usingRecursiveComparison().isEqualTo(list);
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var task = new Task();
        task.setId(1);
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));
        var savedTask = taskService.findById(1);
        assertThat(savedTask.get()).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenFindDoneThenGetSameList() {
        var task = new Task();
        task.setId(1);
        List<Task> list = List.of(task);
        when(taskRepository.findByDone()).thenReturn(list);
        var savedList = taskService.findByDone();
        assertThat(savedList).usingRecursiveComparison().isEqualTo(list);
    }

    @Test
    public void whenFindNewThenGetSameList() {
        var task = new Task();
        task.setId(1);
        List<Task> list = List.of(task);
        when(taskRepository.findByNew()).thenReturn(list);
        var savedList = taskService.findByNew();
        assertThat(savedList).usingRecursiveComparison().isEqualTo(list);
    }

    @Test
    public void whenSaveTaskThenGetSameTask() {
        var task = new Task();
        task.setId(1);
        when(taskRepository.save(task)).thenReturn(task);
        var savedTask = taskService.save(task);
        assertThat(savedTask).usingRecursiveComparison().isEqualTo(task);
    }

    @Test
    public void whenUpdateTaskThenGetTrue() {
        var task = new Task();
        task.setId(1);
        when(taskRepository.update(task)).thenReturn(true);
        var result = taskService.update(task);
        assertThat(result).isTrue();
    }

    @Test
    public void whenDeleteTaskThenGetTrue() {
        var task = new Task();
        task.setId(1);
        when(taskRepository.deleteById(task.getId())).thenReturn(true);
        var result = taskService.deleteById(task.getId());
        assertThat(result).isTrue();
    }

}