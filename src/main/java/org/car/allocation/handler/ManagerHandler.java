package org.car.allocation.handler;

import org.car.allocation.util.UserRole;
import org.car.allocation.model.User;

import java.util.Scanner;

public class ManagerHandler {
    private final Scanner scanner;

    public ManagerHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void handleLogin() {
        System.out.println("\nPlease enter your username:");
        String username = scanner.nextLine();

        User manager = new User(username, UserRole.MANAGER);

        System.out.println("Welcome " + username + "! You are logged in as a MANAGER.");
        System.out.println("You can reserve vehicles, view available vehicles, and view vehicle status.");

        showManagerOptions();
    }

    private void showManagerOptions() {
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Reserve a vehicle");
            System.out.println("2. View available vehicles");
            System.out.println("3. View vehicle status");
            System.out.println("4. Go back to main menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("You chose option 1: Reserve a vehicle.");
                    askToReturnToOptions();
                    break;
                case 2:
                    System.out.println("You chose option 2: View available vehicles.");
                    askToReturnToOptions();
                    break;
                case 3:
                    System.out.println("You chose option 3: View vehicle status.");
                    askToReturnToOptions();
                    break;
                case 4:
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
