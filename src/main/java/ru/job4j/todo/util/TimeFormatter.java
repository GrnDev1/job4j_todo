package ru.job4j.todo.util;

import ru.job4j.todo.model.Task;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    public static String getFormatTime(Task task) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm yyyy-MM-dd");
        ZoneId userZoneId = ZoneId.of(task.getUser().getTimeZone());
        ZoneId defaultZoneId = ZoneId.systemDefault();
        ZonedDateTime defaultTime = task.getCreated().atZone(defaultZoneId);
        return defaultTime.withZoneSameInstant(userZoneId).format(formatter);
    }
}
