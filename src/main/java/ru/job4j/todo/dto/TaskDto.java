package ru.job4j.todo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(builderMethodName = "of")
public class TaskDto {
    @EqualsAndHashCode.Include
    private int id;
    private String title;
    private String description;
    private String created;
    private String username;
    private String status;
}
