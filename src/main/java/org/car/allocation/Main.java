package org.car.allocation;

import org.car.allocation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        //Open a new session
        Session session = sessionFactory.openSession();

        //Close the session
        session.close();

        //Shut down the SessionFactory
        HibernateUtil.shutdown();
    }
}