package org.car.allocation.repository;

import org.car.allocation.model.User;
import org.car.allocation.singleton.DatabaseUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

/**
 * The UserRepository class provides methods for performing CRUD operations
 * on the User entity in the database using Hibernate.
 * It handles the interaction with the database, allowing you to find, save,
 * update, and delete User objects. All database operations are encapsulated
 * in transactions to ensure data integrity.
 */
public class UserRepository {
    /**
     * Finds a user by their unique identifier (ID).
     *
     * @param id the ID of the user to find
     * @return an Optional containing the User if found, or an empty Optional if not
     */
    public Optional<User> findById(long id) {
        try (Session session = DatabaseUtil.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a List of all User objects in the database
     */
    public List<User> findAll() {
        try (Session session = DatabaseUtil.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    /**
     * Saves a new User to the database.
     * This method executes a transaction that persists the provided User entity
     * to the database.
     * @param user the User object to be saved
     */
    public void save(User user) {
        DatabaseUtil.executeTransaction(session -> session.save(user));
    }

    /**
     * Updates an existing User in the database.
     * This method executes a transaction that updates the User entity in the
     * database with the provided changes.
     * @param user the User object to be updated
     */
    public void update(User user) {
        DatabaseUtil.executeTransaction(session -> session.update(user));
    }

    /**
     * Deletes a User from the database.
     * This method executes a transaction that removes the provided User entity
     * from the database.
     * @param user the User object to be deleted
     */
    public void delete(User user) {
        DatabaseUtil.executeTransaction(session -> session.delete(user));
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the User if found, or an empty Optional if not
     */
    public Optional<User> findByUsername(String username) {
        try (Session session = DatabaseUtil.openSession()) {
            Query<User> query = session.createQuery("from User u where u.username = :username", User.class);
            query.setParameter("username", username);
            return query.uniqueResultOptional();
        }
    }
}
