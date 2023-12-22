package ru.job4j.todo.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
public class SimpleTimeZoneService implements TimeZoneService {
    @Override
    public List<TimeZone> getZones() {
        var zones = new ArrayList<TimeZone>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }

    @Override
    public TimeZone getDefaultZone() {
        return TimeZone.getDefault();
    }
}
