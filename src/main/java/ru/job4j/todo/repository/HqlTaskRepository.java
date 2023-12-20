package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class HqlTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Task> findAll() {
        return crudRepository.query("FROM Task ORDER BY id", Task.class);
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task f JOIN FETCH f.priority where f.id = :fId", Task.class,
                Map.of("fId", id)
        );
    }

    @Override
    public List<Task> findByDone() {
        return crudRepository.query("FROM Task WHERE done = true ORDER BY id", Task.class);
    }

    @Override
    public List<Task> findByNew() {
        return crudRepository.query("FROM Task WHERE done = false ORDER BY id", Task.class);
    }

    @Override
    public Task save(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public boolean update(Task task) {
        Function<Session, Boolean> command = session ->
                session.createQuery("UPDATE Task "
                                + ("SET title=:title, description=:description, priority.id =:Id ")
                                + ("WHERE id=:id"))
                        .setParameter("title", task.getTitle())
                        .setParameter("description", task.getDescription())
                        .setParameter("Id", task.getPriority().getId())
                        .setParameter("id", task.getId())
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }

    @Override
    public boolean completeTask(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("UPDATE Task "
                                + ("SET done=:done ")
                                + ("WHERE id=:id"))
                        .setParameter("done", true)
                        .setParameter("id", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }

    @Override
    public boolean deleteById(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE Task WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }
}