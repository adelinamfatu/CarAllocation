package org.car.allocation.repository;

import org.car.allocation.model.Vehicle;
import org.car.allocation.util.DatabaseUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class VehicleRepository<T extends Vehicle> {
    private final Class<T> type;

    public VehicleRepository(Class<T> type) {
        this.type = type;
    }

    public Optional<T> findById(int id) {
        try (Session session = DatabaseUtil.openSession()) {
            T result = session.get(type, id);
            return Optional.ofNullable(result);
        }
    }

    public List<T> findAll() {
        try (Session session = DatabaseUtil.openSession()) {
            return session.createQuery("from " + type.getSimpleName(), type).list();
        }
    }

    public void save(T entity) {
        DatabaseUtil.executeTransaction(session -> session.save(entity));
    }

    public void update(T entity) {
        DatabaseUtil.executeTransaction(session -> session.update(entity));
    }

    public void delete(T entity) {
        DatabaseUtil.executeTransaction(session -> session.delete(entity));
    }
}
