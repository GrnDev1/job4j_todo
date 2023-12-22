package ru.job4j.todo.mappers;

import org.mapstruct.Mapper;
import ru.job4j.todo.dto.TaskDto;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TaskMapper {

    default TaskDto getTaskFromEntity(Task task) {
        String status = task.isDone() ? "Completed" : "In Progress";
        return TaskDto.of()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .created(TimeFormatter.getFormatTime(task))
                .username(task.getUser().getName())
                .status(status)
                .build();
    }

    default List<TaskDto> getListFromEntity(List<Task> tasks) {
        List<TaskDto> list = new ArrayList<>();
        for (Task task : tasks) {
            list.add(getTaskFromEntity(task));
        }
        return list;
    }
}
