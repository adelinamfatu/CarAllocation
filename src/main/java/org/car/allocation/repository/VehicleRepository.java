package org.car.allocation.repository;

import org.car.allocation.model.Vehicle;
import org.car.allocation.singleton.DatabaseUtil;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

/**
 * The VehicleRepository class provides generic methods for performing CRUD operations
 * on any type of Vehicle entity in the database using Hibernate.
 * This class supports operations such as finding, saving, updating, and deleting vehicles.
 * It is designed to work with any subclass of Vehicle.
 *
 * @param <T> the type of vehicle (e.g., Car, Truck) that this repository manages
 */
public class VehicleRepository<T extends Vehicle> {
    private final Class<T> type;

    /**
     * Constructs a VehicleRepository for a specific vehicle type.
     *
     * @param type the class type of the vehicle (e.g., Car.class or Truck.class)
     */
    public VehicleRepository(Class<T> type) {
        this.type = type;
    }

    /**
     * Finds a vehicle by its unique identifier (ID).
     *
     * @param id the ID of the vehicle to find
     * @return an Optional containing the vehicle if found, or an empty Optional if not
     */
    public Optional<T> findById(int id) {
        try (Session session = DatabaseUtil.openSession()) {
            T result = session.get(type, id);
            return Optional.ofNullable(result);
        }
    }

    /**
     * Retrieves all vehicles of the specified type from the database.
     * @return a List of all vehicles of the specified type
     */
    public List<T> findAll() {
        try (Session session = DatabaseUtil.openSession()) {
            return session.createQuery("from " + type.getSimpleName(), type).list();
        }
    }

    /**
     * Saves a new vehicle entity to the database.
     * @param entity the vehicle to be saved
     */
    public void save(T entity) {
        DatabaseUtil.executeTransaction(session -> session.save(entity));
    }

    /**
     * Updates an existing vehicle entity in the database.
     *
     * @param entity the vehicle to be updated
     */
    public void update(T entity) {
        DatabaseUtil.executeTransaction(session -> session.update(entity));
    }

    /**
     * Deletes a vehicle entity from the database.
     *
     * @param entity the vehicle to be deleted
     */
    public void delete(T entity) {
        DatabaseUtil.executeTransaction(session -> session.delete(entity));
    }
}
