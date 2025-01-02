package org.car.allocation.handler;

import org.car.allocation.abstract_factory.CarFactory;
import org.car.allocation.abstract_factory.TruckFactory;
import org.car.allocation.abstract_factory.VehicleFactory;
import org.car.allocation.model.Car;
import org.car.allocation.model.Truck;
import org.car.allocation.model.Vehicle;
import org.car.allocation.service.VehicleService;
import org.car.allocation.util.EngineType;
import org.car.allocation.util.UserRole;
import org.car.allocation.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminHandler {
    private final Scanner scanner;
    private final VehicleService<Vehicle> vehicleService = new VehicleService<>();

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
                    viewAllVehicles();
                    askToReturnToOptions();
                    break;
                case 2:
                    System.out.println("You chose option 2: View vehicle status.");
                    askToReturnToOptions();
                    break;
                case 3:
                    System.out.println("You chose option 3: Add a new vehicle.");
                    addNewVehicle();
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

    public void addNewVehicle() {
        System.out.println("\nWhat type of vehicle would you like to add?");
        System.out.println("1. Car");
        System.out.println("2. Truck");

        int typeChoice = scanner.nextInt();
        scanner.nextLine();

        VehicleFactory factory;
        Vehicle vehicle;

        switch (typeChoice) {
            case 1:
                System.out.println("Enter passenger capacity for the car:");
                int passengerCapacity = scanner.nextInt();
                scanner.nextLine();
                factory = new CarFactory(passengerCapacity);

                System.out.println("Enter license plate:");
                String carLicensePlate = scanner.nextLine();

                System.out.println("Enter model:");
                String carModel = scanner.nextLine();

                System.out.println("Enter fuel level:");
                double carFuelLevel = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Enter engine type (PETROL, DIESEL, ELECTRIC):");
                String carEngineType = scanner.nextLine().toUpperCase();

                vehicle = factory.createVehicle(carLicensePlate, carModel, carFuelLevel, EngineType.valueOf(carEngineType));
                vehicleService.addCar((Car) vehicle);
                System.out.println("Car added successfully!");
                break;

            case 2:
                System.out.println("Enter cargo capacity for the truck:");
                double cargoCapacity = scanner.nextDouble();
                scanner.nextLine();
                factory = new TruckFactory(cargoCapacity);

                System.out.println("Enter license plate:");
                String truckLicensePlate = scanner.nextLine();

                System.out.println("Enter model:");
                String truckModel = scanner.nextLine();

                System.out.println("Enter fuel level:");
                double truckFuelLevel = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Enter engine type (PETROL, DIESEL, ELECTRIC):");
                String truckEngineType = scanner.nextLine().toUpperCase();

                vehicle = factory.createVehicle(truckLicensePlate, truckModel, truckFuelLevel, EngineType.valueOf(truckEngineType));
                vehicleService.addTruck((Truck) vehicle);
                System.out.println("Truck added successfully!");
                break;

            default:
                System.out.println("Invalid option, returning to menu.");
                break;
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
