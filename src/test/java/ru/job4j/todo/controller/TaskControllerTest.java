package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskDtoService;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    private final TaskService taskService = mock(TaskService.class);
    private final TaskDtoService taskDtoService = mock(TaskDtoService.class);
    private final PriorityService priorityService = mock(PriorityService.class);
    private final CategoryService categoryService = mock(CategoryService.class);
    private final TaskController taskController = new TaskController(taskService, taskDtoService, priorityService, categoryService);

    @Test
    public void whenRequestTaskListPageThenGetPageWithAllTasks() {
        var taskDto1 = TaskDto.of()
                .id(1)
                .status("Completed")
                .build();
        var taskDto2 = TaskDto.of()
                .id(2)
                .status("In Progress")
                .build();
        var expectedTasks = List.of(taskDto1, taskDto2);
        when(taskDtoService.findAll()).thenReturn(expectedTasks);

        var model = new ConcurrentModel();
        var view = taskController.getAll(model);
        var actualTasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenRequestTaskListPageThenGetPageWithDoneTasks() {
        var taskDto1 = TaskDto.of()
                .id(1)
                .status("Completed")
                .build();
        var taskDto2 = TaskDto.of()
                .id(2)
                .status("In Progress")
                .build();
        var expectedTasks = List.of(taskDto1, taskDto2);
        when(taskDtoService.findByDone()).thenReturn(expectedTasks);

        var model = new ConcurrentModel();
        var view = taskController.getDone(model);
        var actualTasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenRequestTaskListPageThenGetPageWithNewTasks() {
        var taskDto1 = TaskDto.of()
                .id(1)
                .status("Completed")
                .build();
        var taskDto2 = TaskDto.of()
                .id(2)
                .status("In Progress")
                .build();
        var expectedTasks = List.of(taskDto1, taskDto2);
        when(taskDtoService.findByNew()).thenReturn(expectedTasks);

        var model = new ConcurrentModel();
        var view = taskController.getNew(model);
        var actualTasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actualTasks).isEqualTo(expectedTasks);
    }

    @Test
    public void whenRequestTaskCreationPageThenGetCreatePage() {
        var model = new ConcurrentModel();
        assertThat(taskController.getCreationPage(model)).isEqualTo("tasks/create");
    }

    @Test
    public void whenPostCreateTaskThenSameDataAndRedirectToTasksPage() {
        var task1 = new Task();
        task1.setId(1);
        task1.setDone(true);
        var taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        when(taskService.save(taskArgumentCaptor.capture())).thenReturn(task1);
        var view = taskController.create(task1, mock(HttpSession.class), List.of(1, 2, 3));
        var actualTask = taskArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/tasks");
        assertThat(actualTask).usingRecursiveComparison().isEqualTo(task1);
    }

    @Test
    public void whenRequestTaskGetByIdThenGetPageWithTask() {
        var taskDto1 = TaskDto.of()
                .id(1)
                .status("Completed")
                .build();
        when(taskDtoService.findById(1)).thenReturn(Optional.of(taskDto1));
        var model = new ConcurrentModel();
        var view = taskController.getById(model, 1);
        var actualTask = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/one");
        assertThat(actualTask).usingRecursiveComparison().isEqualTo(taskDto1);
    }

    @Test
    public void whenSomeExceptionGetByIdThenGetErrorPageWithMessage() {
        var model = new ConcurrentModel();
        var view = taskController.getById(model, 1);
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo("Task with this id is not found");
    }

    @Test
    public void whenRequestTaskEditThenGetPageWithTask() {
        var task1 = new Task();
        task1.setId(1);
        task1.setDone(true);
        when(taskService.findById(1)).thenReturn(Optional.of(task1));
        var model = new ConcurrentModel();
        var view = taskController.edit(model, 1);
        var actualTask = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/edit");
        assertThat(actualTask).usingRecursiveComparison().isEqualTo(task1);
    }

    @Test
    public void whenSomeExceptionEditThenGetErrorPageWithMessage() {
        var model = new ConcurrentModel();
        var view = taskController.edit(model, 1);
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo("Task with this id is not found");
    }

    @Test
    public void whenPostUpdateTaskThenSameDataAndRedirectToTasksPage() {
        var task1 = new Task();
        task1.setId(1);
        task1.setDone(true);
        var taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);
        when(taskService.update(taskArgumentCaptor.capture())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.update(task1, model, List.of(1, 2, 3));
        var actualTask = taskArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/tasks");
        assertThat(actualTask).usingRecursiveComparison().isEqualTo(task1);
    }

    @Test
    public void whenSomeExceptionUpdateThenGetErrorPageWithMessage() {
        var task1 = new Task();
        task1.setId(1);
        task1.setDone(true);
        var model = new ConcurrentModel();
        var view = taskController.update(task1, model, List.of(1, 2, 3));
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo("Task with this id is not found");
    }

    @Test
    public void whenRequestTaskDeleteThenRedirectToTasks() {
        int id = 1;
        var idArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        when(taskService.deleteById(idArgumentCaptor.capture())).thenReturn(true);
        var model = new ConcurrentModel();
        var view = taskController.delete(model, id);
        var actualId = idArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/tasks");
        assertThat(actualId).usingRecursiveComparison().isEqualTo(id);
    }

    @Test
    public void whenSomeExceptionDeleteThenGetErrorPageWithMessage() {
        int id = 1;
        var model = new ConcurrentModel();
        when(taskService.deleteById(id)).thenReturn(false);
        var view = taskController.delete(model, id);
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo("Task with this id is not found");
    }

}