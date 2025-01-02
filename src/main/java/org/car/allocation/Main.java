package org.car.allocation;

import org.car.allocation.handlers.AdminHandler;
import org.car.allocation.handlers.ManagerHandler;
import org.car.allocation.handlers.UserHandler;
import org.car.allocation.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        System.out.println("********************************************************");
        System.out.println("WELCOME TO THE VEHICLE ALLOCATION SYSTEM");
        System.out.println("This system will help you manage vehicle availability.");
        System.out.println("********************************************************");

        showMainMenu();

        session.close();
        HibernateUtil.shutdown();
    }

    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        while (!exit) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Log in as a USER (Driver)");
            System.out.println("2. Log in as a MANAGER");
            System.out.println("3. Log in as an ADMIN");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumă newline-ul

            switch (choice) {
                case 1:
                    new UserHandler(scanner).handleLogin();
                    break;
                case 2:
                    new ManagerHandler(scanner).handleLogin();
                    break;
                case 3:
                    new AdminHandler(scanner).handleLogin();
                    break;
                case 4:
                    System.out.println("Exiting the system...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    break;
            }
        }
    }
}
