package org.car.allocation;

import org.car.allocation.handler.UserHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.car.allocation.singleton.HibernateUtil;
import org.car.allocation.util.UserRole;

import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        System.out.println(messages.getString("system.welcome"));

        showMainMenu();

        session.close();
        HibernateUtil.shutdown();
    }

    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        UserHandler userHandler = new UserHandler(scanner);

        boolean exit = false;

        while (!exit) {
            System.out.println("\n" + messages.getString("main.option.prompt"));
            System.out.println(messages.getString("main.option.login_driver"));
            System.out.println(messages.getString("main.option.login_manager"));
            System.out.println(messages.getString("main.option.login_admin"));
            System.out.println(messages.getString("main.option.signin"));
            System.out.println(messages.getString("main.option.exit"));

            int choice = scanner.nextInt();
            scanner.nextLine();

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
                    System.out.println(messages.getString("main.select_role"));
                    System.out.println("1. Driver");
                    System.out.println("2. Manager");
                    System.out.println("3. Admin");
                    int roleChoice = scanner.nextInt();
                    scanner.nextLine();

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
                            System.out.println(messages.getString("main.invalid_role"));
                            break;
                    }
                    break;
                case 5:
                    System.out.println(messages.getString("main.exiting"));
                    exit = true;
                    break;
                default:
                    System.out.println(messages.getString("invalid.option"));
                    break;
            }
        }
    }
}
