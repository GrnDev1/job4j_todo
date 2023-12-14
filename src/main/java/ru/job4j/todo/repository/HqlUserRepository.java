package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HqlUserRepository implements UserRepository {
    private final SessionFactory sf;
    private static final Logger LOG = LoggerFactory.getLogger(HqlUserRepository.class);

    @Override
    public Optional<User> save(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
                return Optional.of(user);
            } catch (Exception e) {
                session.getTransaction().rollback();
                LOG.error("User with this mail already exists", e);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> result = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("FROM User WHERE login=:login AND password=:password", User.class)
                        .setParameter("login", login)
                        .setParameter("password", password)
                        .uniqueResultOptional();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }
}