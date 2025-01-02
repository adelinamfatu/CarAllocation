package org.car.allocation.handlers;

import org.car.allocation.util.UserRole;
import org.car.allocation.observer.User;

import java.util.Scanner;

public class AdminHandler {
    private final Scanner scanner;

    public AdminHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void handleLogin() {
        System.out.println("\nPlease enter your username:");
        String username = scanner.nextLine();

        User admin = new User(username, UserRole.ADMIN);

        System.out.println("Welcome " + username + "! You are logged in as an ADMIN.");
        System.out.println("You have full access to all operations: view, add, and delete vehicles.");

        showAdminOptions();
    }

    private void showAdminOptions() {
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. View all vehicles");
            System.out.println("2. View vehicle status");
            System.out.println("3. Add a new vehicle");
            System.out.println("4. Delete a vehicle");
            System.out.println("5. Go back to main menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("You chose option 1: View all vehicles.");
                    askToReturnToOptions();
                    break;
                case 2:
                    System.out.println("You chose option 2: View vehicle status.");
                    askToReturnToOptions();
                    break;
                case 3:
                    System.out.println("You chose option 3: Add a new vehicle.");
                    askToReturnToOptions();
                    break;
                case 4:
                    System.out.println("You chose option 4: Delete a vehicle.");
                    askToReturnToOptions();
                    break;
                case 5:
                    backToMenu = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    break;
            }
        }
    }

    private void askToReturnToOptions() {
        System.out.println("\nWould you like to return to the options? (Yes/No)");
        String response = scanner.nextLine().toLowerCase();

        if (response.equals("yes")) {
            return;
        } else if (response.equals("no")) {
            System.out.println("Returning to previous options...");
        } else {
            System.out.println("Invalid response, please type 'Yes' or 'No'.");
            askToReturnToOptions();
        }
    }
}
