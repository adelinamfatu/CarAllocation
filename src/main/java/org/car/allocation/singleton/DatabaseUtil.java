package org.car.allocation.singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DatabaseUtil {
    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    /**
     * Opens a new Hibernate session.
     * @return a new session
     */
    public static Session openSession() {
        return sessionFactory.openSession();
    }

    /**
     * Executes a database operation within a transaction.
     * @param operation the operation to execute
     */
    public static void executeTransaction(DatabaseOperation operation) {
        Transaction transaction = null;
        try (Session session = openSession()) {
            transaction = session.beginTransaction();
            operation.execute(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Database transaction failed", e);
        }
    }

    /**
     * Functional interface for database operations.
     */
    @FunctionalInterface
    public interface DatabaseOperation {
        void execute(Session session);
    }
}
