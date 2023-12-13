package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HqlTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public List<Task> findAll() {
        List<Task> result = List.of();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("FROM Task ORDER BY id", Task.class).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    @Override
    public Optional<Task> findById(int id) {
        Optional<Task> task = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                task = Optional.of(session.get(Task.class, id));
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return task;
    }

    @Override
    public List<Task> findByDone() {
        List<Task> result = List.of();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("FROM Task WHERE done = true ORDER BY id", Task.class).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    @Override
    public List<Task> findByNew() {
        List<Task> result = List.of();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("FROM Task WHERE done = false ORDER BY id", Task.class).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    @Override
    public Task save(Task task) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(task);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("UPDATE Task "
                                + ("SET title=:title, description=:description, done=:done ")
                                + ("WHERE id=:id"))
                        .setParameter("title", task.getTitle())
                        .setParameter("description", task.getDescription())
                        .setParameter("done", task.isDone())
                        .setParameter("id", task.getId())
                        .executeUpdate() != 0;
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    @Override
    public boolean deleteById(int id) {
        boolean result = false;
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery(
                                "DELETE Task WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}