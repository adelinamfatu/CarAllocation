package org.car.allocation;

import org.car.allocation.handler.MenuHandler;
import org.car.allocation.handler.UserHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.car.allocation.util.HibernateUtil;
import org.car.allocation.util.UserRole;

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
        UserHandler userHandler = new UserHandler(scanner);

        boolean exit = false;

        while (!exit) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Login as Driver");
            System.out.println("2. Login as Manager");
            System.out.println("3. Login as Admin");
            System.out.println("4. Sign In (Create a new user)");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    userHandler.handleLogin(UserRole.DRIVER);
                    break;
                case 2:
                    userHandler.handleLogin(UserRole.MANAGER);
                    break;
                case 3:
                    userHandler.handleLogin(UserRole.ADMIN);
                    break;
                case 4:
                    System.out.println("Select role for new user:");
                    System.out.println("1. Driver");
                    System.out.println("2. Manager");
                    System.out.println("3. Admin");
                    int roleChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (roleChoice) {
                        case 1:
                            userHandler.handleSignIn(UserRole.DRIVER);
                            break;
                        case 2:
                            userHandler.handleSignIn(UserRole.MANAGER);
                            break;
                        case 3:
                            userHandler.handleSignIn(UserRole.ADMIN);
                            break;
                        default:
                            System.out.println("Invalid role selected.");
                            break;
                    }
                    break;
                case 5:
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
