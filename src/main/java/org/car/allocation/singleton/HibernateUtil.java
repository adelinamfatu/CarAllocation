package org.car.allocation.singleton;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateUtil is a utility class for managing the Hibernate SessionFactory.
 * This class implements the Singleton pattern to ensure that only one
 * instance of the SessionFactory is created and shared across the application.
 * The SessionFactory is eagerly initialized at the time of class loading,
 * ensuring thread safety and immediate availability but potentially consuming
 * resources even if it is not used.
 */
public class HibernateUtil {
    //The SessionFactory instance used to create Hibernate sessions
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the SessionFactory by reading the configuration file.
     * If the SessionFactory creation fails, it prints an error message
     * and throws an ExceptionInInitializerError.
     *
     * @return A new SessionFactory instance.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Provides the SessionFactory instance.
     * @return The SessionFactory instance.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Shuts down the Hibernate SessionFactory by closing all sessions.
     * It should be called when Hibernate is no longer needed to release resources.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
