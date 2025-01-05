package org.car.allocation;

import org.car.allocation.handler.MenuHandler;
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

        boolean exit = false;

        while (!exit) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1. Login");
            System.out.println("2. Sign In (Create a new user)");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); //Consume newline

            switch (choice) {
                case 1:
                    //Login
                    System.out.println("Choose your role:");
                    System.out.println("1. Driver");
                    System.out.println("2. Manager");
                    System.out.println("3. Admin");
                    int roleChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (roleChoice) {
                        case 1:
                            new MenuHandler(scanner, UserRole.DRIVER).handleLogin();
                            break;
                        case 2:
                            new MenuHandler(scanner, UserRole.MANAGER).handleLogin();
                            break;
                        case 3:
                            new MenuHandler(scanner, UserRole.ADMIN).handleLogin();
                            break;
                        default:
                            System.out.println("Invalid role selected.");
                            break;
                    }
                    break;

                case 2:
                    //Sign In (Create new user)
                    System.out.println("Choose your role for Sign In:");
                    System.out.println("1. Driver");
                    System.out.println("2. Manager");
                    System.out.println("3. Admin");
                    roleChoice = scanner.nextInt();
                    scanner.nextLine(); //Consume newline

                    switch (roleChoice) {
                        case 1:
                            new MenuHandler(scanner, UserRole.DRIVER).handleSignIn();
                            break;
                        case 2:
                            new MenuHandler(scanner, UserRole.MANAGER).handleSignIn();
                            break;
                        case 3:
                            new MenuHandler(scanner, UserRole.ADMIN).handleSignIn();
                            break;
                        default:
                            System.out.println("Invalid role selected.");
                            break;
                    }
                    break;

                case 3:
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
