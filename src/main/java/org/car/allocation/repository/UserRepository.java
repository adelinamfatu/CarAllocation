package org.car.allocation.repository;

import org.car.allocation.model.User;
import org.car.allocation.util.DatabaseUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    public Optional<User> findById(long id) {
        try (Session session = DatabaseUtil.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    public List<User> findAll() {
        try (Session session = DatabaseUtil.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    public void save(User user) {
        DatabaseUtil.executeTransaction(session -> session.save(user));
    }

    public void update(User user) {
        DatabaseUtil.executeTransaction(session -> session.update(user));
    }

    public void delete(User user) {
        DatabaseUtil.executeTransaction(session -> session.delete(user));
    }

    public Optional<User> findByUsername(String username) {
        try (Session session = DatabaseUtil.openSession()) {
            Query<User> query = session.createQuery("from User u where u.username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        }
    }
}
