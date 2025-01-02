package org.car.allocation.handler;

import org.car.allocation.model.Car;
import org.car.allocation.model.Vehicle;
import org.car.allocation.service.VehicleService;
import org.car.allocation.util.EngineType;
import org.car.allocation.util.UserRole;
import org.car.allocation.model.User;

import java.util.List;
import java.util.Scanner;

public class UserHandler {
    private final Scanner scanner;
    private final VehicleService<Vehicle> vehicleService = new VehicleService<>();

    public UserHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public void handleLogin() {
        System.out.println("\nPlease enter your username:");
        String username = scanner.nextLine();

        User user = new User(username, UserRole.DRIVER);

        System.out.println("Welcome " + username + "! You are logged in as a DRIVER.");
        System.out.println("You can reserve vehicles and view available vehicles.");

        showUserOptions();
    }

    private void showUserOptions() {
        boolean backToMenu = false;

        while (!backToMenu) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. View available vehicles");
            System.out.println("2. Reserve a vehicle");
            System.out.println("3. View my reservation history");
            System.out.println("4. Go back to main menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("You chose option 1: View available vehicles.");
                    viewAllVehicles();
                    askToReturnToOptions();
                    break;
                case 2:
                    System.out.println("You chose option 2: Reserve a vehicle.");
                    reserveVehicle();
                    break;
                case 3:
                    System.out.println("You chose option 3: View your reservation history.");
                    viewReservationHistory();
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

    private void viewAllVehicles() {
        System.out.println("\nCars:");
        List<Car> cars = vehicleService.getAllCars();
        for (Car car : cars) {
            System.out.println("- License Plate: " + car.getLicensePlate() +
                    ", Model: " + car.getModel() +
                    ", Fuel Level: " + car.getFuelLevel() +
                    ", Passenger Capacity: " + car.getPassengerCapacity() +
                    ", Engine Type: " + car.getEngineType());
        }
    }

    private void reserveVehicle() {
        String vehicleType = "";
        boolean validVehicleType = false;
        while (!validVehicleType) {
            System.out.println("\nPlease choose the type of vehicle you want to reserve:");
            System.out.println("1. Car");
            System.out.println("2. Truck");


            int vehicleChoice = scanner.nextInt();
            scanner.nextLine();

            switch (vehicleChoice) {
                case 1: vehicleType = "Car"; validVehicleType = true; break;
                case 2: vehicleType = "Truck"; validVehicleType = true; break;
                default:
                    System.out.println("Invalid choice, please choose a valid vehicle type.");
                    break;
            }
        }

        System.out.println("Enter minimum passenger capacity for the " + vehicleType + ":");
        int minCapacity = scanner.nextInt();
        scanner.nextLine();

        String fuelType = "";
        boolean validFuelType = false;
        while (!validFuelType) {
            System.out.println("Enter fuel type (PETROL, DIESEL, ELECTRIC) for the " + vehicleType + ":");
            fuelType = scanner.nextLine().toUpperCase();

            try {
                EngineType.valueOf(fuelType);
                validFuelType = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid fuel type. Please enter a valid fuel type (PETROL, DIESEL, ELECTRIC).");
            }
        }

        System.out.println("Enter the date for reservation (YYYY-MM-DD):");
        String date = scanner.nextLine();

        System.out.println("Enter the duration for the reservation (in days):");
        int duration = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nChecking available vehicles for your criteria...");

        System.out.println("\nYou have successfully reserved a " + vehicleType + " with the following details:");
        System.out.println("Fuel Type: " + fuelType);
        System.out.println("Minimum Passenger Capacity: " + minCapacity + " passengers");
        System.out.println("Reservation Date: " + date);
        System.out.println("Duration: " + duration + " day(s)");

        askToReturnToOptions();
    }

    private void viewReservationHistory() {
        System.out.println("Here is your reservation history: ");
        askToReturnToOptions();
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
